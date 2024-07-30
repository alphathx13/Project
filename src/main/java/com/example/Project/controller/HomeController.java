package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Project.chat.ChatRoomRepository;
import com.example.Project.service.FestivalService;
import com.example.Project.service.FileService;
import com.example.Project.service.MemberService;
import com.example.Project.service.WeatherService;

@Controller
public class HomeController {
	
	@GetMapping("/user/home/main")
	public String showMain() {
		System.out.println();
		return "/user/home/main";
	}

	@GetMapping("/")
	public String root() {
		return "redirect:/user/home/main";
	}
	
}