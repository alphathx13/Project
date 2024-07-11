package com.example.Project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
	private int id;
	private int memberId;
	private int eventSeq;
	private String replyBody;
	private String regDate;
	private String updateDate;
	private String nickname;
	private int likePoint;

	public String getBody() {
		return this.replyBody.replaceAll("\n", "<br/>");
	}
}