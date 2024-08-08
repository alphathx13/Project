package com.example.Project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.Project.config.Sender;

@Service
public class AWSSESService {

	private Sender sender;

	public AWSSESService (Sender sender) {
    	this.sender = sender;
    }
	
	public void send(String subject, String html, List<String> receivers) {
		sender.send(subject, html.toString(), receivers);
	}
	
	public void welcomeMailSend(int id, List<String> receivers) {
		
		String subject = "사이트 가입을 환영합니다.";
    	String templatePath = "templates/welcome.html";

        Map<String, String> variables = new HashMap<>();
        variables.put("joinLink", "http://alphathx13.site/user/member/doJoin?id=" + id);

		sender.templateSend(subject, templatePath, variables, receivers);
	}
	
	public void withdrawalMailSend(int id, List<String> receivers) {
		
		String subject = "회원 탈퇴 메일입니다.";
    	String templatePath = "templates/withdrawal.html";

        Map<String, String> variables = new HashMap<>();
        variables.put("withdrawalLink", "http://alphathx13.site/user/member/doWithdrawal?id=" + id);

		sender.templateSend(subject, templatePath, variables, receivers);
	}
	
	public void passRecoveryMailSend(String tempPassword, List<String> receivers) {
		
		String subject = "비밀번호 찾기 메일입니다.";
    	String templatePath = "templates/passRecovery.html";

        Map<String, String> variables = new HashMap<>();
        variables.put("tempPassword", tempPassword);

		sender.templateSend(subject, templatePath, variables, receivers);
	}
}
