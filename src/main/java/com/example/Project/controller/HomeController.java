package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/home")
	public String showMain() {
		return "/home";
	}

	@GetMapping("/")
	public String root() {
		return "redirect:/home";
	}
	
}