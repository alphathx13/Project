package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/user/home/main")
	public String showMain() {
		return "/user/home/main";
	}

	@GetMapping("/")
	public String root() {
		return "redirect:/user/home/main";
	}
	
	@GetMapping("/chat")
	public String chat() {
		return "/chat/rooms";
	}
	
}