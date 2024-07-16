package com.example.Project.chat;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.Project.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	private final RedisTemplate<String, String> redisTemplate;

	// websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		
		System.out.println(message.getRoomId());
		System.out.println(redisTemplate.opsForList().range(message.getRoomId(), 0, -1));
		
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			chatRoomRepository.enterChatRoom(message.getRoomId());
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		}
		// Websocket에 발행된 메시지를 redis로 발행한다(publish)
		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
	}
}