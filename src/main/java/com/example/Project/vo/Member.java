package com.example.Project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private int id;
	private String regDate;
	private String updateDate;
	private int checkJoin;
	private String loginId;
	private String loginPw;
	private String name;
	private String nickname;
	private String cellphone;
	private String email;
	private int delStatus;
	private int memberImg;
}