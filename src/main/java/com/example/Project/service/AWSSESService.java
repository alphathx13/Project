package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.config.Sender;

@Service
public class AWSSESService {

	private Sender sender;

	public AWSSESService (Sender sender) {
    	this.sender = sender;
    }
	
	public void send(List<String> to, String subject, String html) {
		
		// 보낼 대상
//		for (String item : mailRecipients) {
//			to.add(item);
//		}

		sender.send(subject, html.toString(), to);

	}
}
