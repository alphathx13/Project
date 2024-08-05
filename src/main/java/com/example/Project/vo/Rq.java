package com.example.Project.vo;

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.example.Project.service.FileService;
import com.example.Project.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	
	@Getter
	private int loginMemberNumber;
	@Getter
	private String loginMemberNn;
	@Getter
	private int loginMemberImg;
	@Getter
	private String loginMemberImgPath;
	@Getter
	private HttpServletResponse response;
	private HttpSession session;
	
	public Rq(HttpServletRequest request, HttpServletResponse response, FileService fileService) {
		this.response = response;
		this.session = request.getSession();

		int loginMemberNumber = 0;
		String loginMemberNn = "";
		int loginMemberImg = 0;
		String loginMemberImgPath = "";

		if (session.getAttribute("loginMemberNumber") != null) {
			loginMemberNumber = (int) session.getAttribute("loginMemberNumber");
			loginMemberNn = (String) session.getAttribute("loginMemberNn");
			loginMemberImg = (int) session.getAttribute("loginMemberImg");
		}
		
		this.loginMemberNumber = loginMemberNumber;
		this.loginMemberNn = loginMemberNn;
		this.loginMemberImg = loginMemberImg;
		this.loginMemberImgPath = fileService.getMemberImgPath(loginMemberImg);
		
		request.setAttribute("rq", this);

	}
	
	public void jsReplace(String msg, String uri) {
		response.setContentType("text/html; charset=UTF-8");
		
		try {
			response.getWriter().append(Util.jsReplace(msg, uri));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login(Member member) {
		session.setAttribute("loginMemberNumber", member.getId());
		session.setAttribute("loginMemberNn", member.getNickname());
		session.setAttribute("loginMemberImg", member.getMemberImg());
	}

	public void logout() {
		this.session.removeAttribute("loginMemberNumber");
		this.session.removeAttribute("loginMemberNn");
		this.session.removeAttribute("loginMemberImg");
		this.session.removeAttribute("loginMemberImgPath");
	}
	
	public void init() {
	}
	
}