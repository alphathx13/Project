package com.example.Project.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.Project.vo.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private Map<String, ChatRoom> chatRooms;
    private ListOperations<String, Object> listOps;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
        listOps = redisTemplate.opsForList();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    // 메시지 보내기
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    // 채팅 메시지 저장하기
    public void saveChatMessage(String roomId, ChatMessage message) {
        try {
            listOps.leftPush(getChatMessagesKey(roomId), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("Error serializing chat message: {}", e.getMessage(), e);
        }
    }
    
    // 채팅 메시지를 저장할 키 생성
    private String getChatMessagesKey(String roomId) {
        return "chatroom:" + roomId + ":messages";
    }
    
    // 채팅방에 저장된 내역 가져오기
    public List<ChatMessage> getChatMessages(String roomId) {
        List<Object> messages = listOps.range(getChatMessagesKey(roomId), 0, -1);
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Object obj : messages) {
            try {
                ChatMessage message = objectMapper.readValue((String) obj, ChatMessage.class);
                chatMessages.add(message);
            } catch (IOException e) {
                log.error("Error deserializing chat message: {}", e.getMessage(), e);
            }
        }
        return chatMessages;
    }
}