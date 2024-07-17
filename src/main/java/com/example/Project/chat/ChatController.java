package com.example.Project.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

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

		// 채팅방 입장시
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			
			// 메시지 sub
			chatRoomRepository.enterChatRoom(message.getRoomId());

			// 입장시 이전 채팅내역 보여주기
			List<ChatMessage> messages = chatService.getChatMessages(message.getRoomId());
			
			for (ChatMessage chat : messages) {
				redisPublisher.publish(chatRoomRepository.getTopic(chat.getRoomId()), chat);
			}
			
			// 입장 메시지 입력
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
			redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
			return;
		}
		
		// 메시지 저장하기
		chatService.saveChatMessage(message.getRoomId(), message);
		
		// 메시지 pub
		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
	}
}