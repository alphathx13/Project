package com.example.Project.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

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
	private int wf3Am;
	private int wf3Pm;
	private int wf4Am;
	private int wf4Pm;
	private int wf5Am;
	private int wf5Pm;
	private int wf6Am;
	private int wf6Pm;
	private int wf7Am;
	private int wf7Pm;

//CREATE TABLE `WeatherTemp` (
//	`taMin3`	INT(4)	NOT NULL,
//	`taMax3`	INT(4)	NOT NULL,
//	`taMin4`	INT(4)	NOT NULL,
//	`taMax4`	INT(4)	NOT NULL,
//	`taMin5`	INT(4)	NOT NULL,
//	`taMax5`	INT(4)	NOT NULL,
//	`taMin6`	INT(4)	NOT NULL,
//	`taMax6`	INT(4)	NOT NULL,
//	`taMin7`	INT(4)	NOT NULL,
//	`taMax7`	INT(4)	NOT NULL
//);
}