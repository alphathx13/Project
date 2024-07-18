package com.example.Project.chat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.util.Util;
import com.example.Project.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	
	@Autowired
	public ChatService chatService;
	
	// "/pub/chat/message"로 들어오는 메시지 처리
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		// 시간 설정
		message.setTimestamp(Util.Now());

		// 채팅방 입장시
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			// 메시지 sub
			chatRoomRepository.enterChatRoom(message.getRoomId());

			// 입장 메시지 입력
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
			redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
			return;
		}
		
		// 채팅방 퇴장시
		if (ChatMessage.MessageType.LEAVE.equals(message.getType())) {
			// 퇴장 메시지 입력
			message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
			redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
			return;
		}
		
		// 메시지 저장하기
		chatService.saveChatMessage(message.getRoomId(), message);
		
		// 메시지 pub
		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
	}
	
	@GetMapping("/pub/chat/lastMessage")
	@ResponseBody
	public List<ChatMessage> pastMessages(String roomId) {
		List<ChatMessage> messages = chatService.getChatMessages(roomId);
		Collections.reverse(messages);
		return messages;
	}
	
    // 특정 채팅방의 접속 중인 사용자 목록 조회
    @GetMapping("/chat/room/{roomId}/online-users")
    @ResponseBody
    public Set<String> showOnlineUsersInRoom(@PathVariable String roomId) {
        Set<String> onlineUsers = chatService.getOnlineUsersInRoom(roomId);
        return onlineUsers; 
    }
    
    @GetMapping("/chat/room/{roomId}/connect")
    @ResponseBody
    public void userConnected(@PathVariable String roomId, String userId) {
    	chatService.userConnected(roomId, userId);
    }
    
    @GetMapping("/chat/room/{roomId}/disconnect")
    @ResponseBody
    public void userDisconnected(@PathVariable String roomId, String userId) {
    	chatService.userDisconnected(roomId, userId);
    }
}