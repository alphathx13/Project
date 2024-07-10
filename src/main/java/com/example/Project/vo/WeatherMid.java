package com.example.Project.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMid {
	
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
	private int taMin3;	
	private int taMax3;
	private int taMin4;	
	private int taMax4;
	private int taMin5;	
	private int taMax5;	
	private int taMin6;	
	private int taMax6;	
	private int taMin7;
	private int taMax7;	
}