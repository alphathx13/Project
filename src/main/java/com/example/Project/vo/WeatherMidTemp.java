package com.example.Project.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMidTemp {
	
	private String updateTime;
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