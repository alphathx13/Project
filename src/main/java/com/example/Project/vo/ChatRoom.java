package com.example.Project.vo;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom {

    private String roomId;
    private String name;

    public static ChatRoom create(String name){
        ChatRoom room = new ChatRoom();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}