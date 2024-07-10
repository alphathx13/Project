package com.example.Project.vo;

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

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
	private HttpServletResponse response;
	private HttpSession session;

	public Rq(HttpServletRequest request, HttpServletResponse response) {
		
		this.response = response;
		this.session = request.getSession();

		int loginMemberNumber = 0;
		String loginMemberNn = "";

		if (session.getAttribute("loginMemberNumber") != null) {
			loginMemberNumber = (int) session.getAttribute("loginMemberNumber");
			loginMemberNn = (String) session.getAttribute("loginMemberNn");
		}
		
		this.loginMemberNumber = loginMemberNumber;
		this.loginMemberNn = loginMemberNn;
		
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
	}

	public void logout() {
		this.session.removeAttribute("loginMemberNumber");
		this.session.removeAttribute("loginMemberNn");
	}
	
	public void init() {
	}
	
}