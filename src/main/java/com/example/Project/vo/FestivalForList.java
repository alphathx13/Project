package com.example.Project.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FestivalForList {
	private int eventSeq;
	private String themeCdNm;
	private String title;
	private String beginDt;
	private String endDt;
	private int likePoint;
	private int viewCount;
}