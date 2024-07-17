package com.example.Project.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisUserService {

    private static final String ONLINE_USERS_PREFIX = "online_users:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 특정 채팅방의 접속 중인 사용자 목록 조회
    public Set<String> getOnlineUsersInRoom(String roomId) {
        String key = ONLINE_USERS_PREFIX + roomId;
        return redisTemplate.opsForSet().members(key);
    }

    // 사용자가 특정 채팅방에 접속
    public void userConnected(String roomId, String userId) {
        String key = ONLINE_USERS_PREFIX + roomId;
        redisTemplate.opsForSet().add(key, userId);
    }

    // 사용자가 특정 채팅방에서 접속 해제
    public void userDisconnected(String roomId, String userId) {
        String key = ONLINE_USERS_PREFIX + roomId;
        redisTemplate.opsForSet().remove(key, userId);
    }
}
