package com.example.Project.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK
    }
    
    private MessageType type; 
    private String roomId;
    private String sender; 
    private String message;
    private String timestamp; 
}