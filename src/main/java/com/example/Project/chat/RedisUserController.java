package com.example.Project.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class RedisUserController {

    @Autowired
    private RedisUserService redisUserService;

    // 특정 채팅방의 접속 중인 사용자 목록 조회
    @GetMapping("/chat/room/{roomId}/online-users")
    @ResponseBody
    public Set<String> showOnlineUsersInRoom(@PathVariable String roomId) {
        Set<String> onlineUsers = redisUserService.getOnlineUsersInRoom(roomId);
        return onlineUsers; 
    }
    
    @GetMapping("/chat/room/{roomId}/connect")
    @ResponseBody
    public void userConnected(@PathVariable String roomId, String userId) {
    	redisUserService.userConnected(roomId, userId);
    }
    
    @GetMapping("/chat/room/{roomId}/disconnect")
    @ResponseBody
    public void userDisconnected(@PathVariable String roomId, String userId) {
    	redisUserService.userDisconnected(roomId, userId);
    }
}
