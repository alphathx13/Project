package com.example.Project.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMidRain {
	
	private String updateTime;
	private int rnSt3Am;
	private int rnSt3Pm;
	private int rnSt4Am;
	private int rnSt4Pm;
	private int rnSt5Am;
	private int rnSt5Pm;
	private int rnSt6Am;
	private int rnSt6Pm;
	private int rnSt7Am;
	private int rnSt7Pm;
	private String wf3Am;
	private String wf3Pm;
	private String wf4Am;
	private String wf4Pm;
	private String wf5Am;
	private String wf5Pm;
	private String wf6Am;
	private String wf6Pm;
	private String wf7Am;
	private String wf7Pm;
}