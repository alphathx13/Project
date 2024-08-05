package com.example.Project.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Project.service.AwsSesService;

@Controller
public class AwsSesController {

	private AwsSesService awsSesService;

	public AwsSesController(AwsSesService awsSesService) {
		this.awsSesService = awsSesService;
	}
	
	@GetMapping("/email")
	public String write() {
		String subject = "제목입니다.";
        String to = "kjh@test.com";
        Map<String, Object> variables = Map.of("data", "안녕하세요");
 
        awsSesService.send(subject, variables, to);
		return "/";
	}

}