package com.example.Project.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.ChatService;
import com.example.Project.service.FileService;
import com.example.Project.service.MemberService;
import com.example.Project.util.Util;
import com.example.Project.vo.ChatMessage;
import com.example.Project.vo.Rq;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;
	
	@Autowired
	private MemberService memberService;
	
	// "/pub/chat/message"로 들어오는 message 처리
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		
		// message의 시간, 닉네임 설정
		message.setTimestamp(Util.Now());
		message.setNickname(memberService.getNicknameById(Integer.valueOf(message.getSender())));
		
		// 채팅방 입장/퇴장시
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			chatRoomRepository.enterChatRoom(message.getRoomId());
			
			message.setMessage("님이 입장하셨습니다.");
			redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
			return;
		} else if (ChatMessage.MessageType.LEAVE.equals(message.getType())) {
			message.setMessage("님이 퇴장하셨습니다.");
			redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
			return;
		}
		
		// 메시지 저장
		chatService.saveChatMessage(message.getRoomId(), message);
		
		// 입력한 메시지 pub
		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
	}
	
	// 이전 채팅내역 가져오기
	@GetMapping("/pub/chat/lastMessage")
	@ResponseBody
	public List<ChatMessage> pastMessages(String roomId) {
		List<ChatMessage> messages = chatService.getChatMessages(roomId);
		Collections.reverse(messages);
		
		for (int i = 0; i < messages.size(); i++) {
			ChatMessage message = messages.get(i);
			message.setNickname(memberService.getNicknameById(Integer.valueOf(message.getSender())));
		}
		
		return messages;
	}
	
    // 채팅방의 접속 중인 사용자 목록 조회
    @GetMapping("/chat/room/{roomId}/online-users")
    @ResponseBody
    public List<String> showOnlineUsersInRoom(@PathVariable String roomId) {
        
    	List<String> onlineUsers = new ArrayList<>(chatService.getOnlineUsersInRoom(roomId));
    	
    	for (int i = 0; i < onlineUsers.size(); i++) {
        	onlineUsers.set(i, memberService.getNicknameById(Integer.valueOf(onlineUsers.get(i))));
        }
    	
        return onlineUsers; 
    }
    
    // 채팅방 접속
    @GetMapping("/chat/room/{roomId}/connect")
    @ResponseBody
    public void userConnected(@PathVariable String roomId, String userId) {
    	chatService.userConnected(roomId, userId);
    }

    // 채팅방 퇴장
    @GetMapping("/chat/room/{roomId}/disconnect")
    @ResponseBody
    public void userDisconnected(@PathVariable String roomId, String userId) {
    	chatService.userDisconnected(roomId, userId);
    }
}