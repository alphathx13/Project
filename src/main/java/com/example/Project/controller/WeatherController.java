package com.example.Project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.WeatherService;
import com.example.Project.vo.WeatherMid;
import com.example.Project.vo.WeatherShort;

@Controller
public class WeatherController {
	
	private WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}
	
	@GetMapping("/user/weather/weatherMidView")
	@ResponseBody
	public WeatherMid weatherMidView(){
		return weatherService.weatherMidView();
	}

	@GetMapping("/user/weather/weatherShortView")
	@ResponseBody
	public List<WeatherShort> weatherShortView(){
		return weatherService.weatherShortView();
	}
}