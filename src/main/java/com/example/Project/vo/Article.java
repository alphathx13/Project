package com.example.Project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	private int id;
	private String title;
	private String body;
	private String regDate;
	private String updateDate;
	private int memberNumber;
	private String writerName;
	private int boardId;
	private int viewCount;
	private int likePoint;
	
	public String getBody() {
		return this.body.replaceAll("\n", "<br />");
	}
}