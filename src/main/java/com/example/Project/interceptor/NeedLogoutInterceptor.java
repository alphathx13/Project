package com.example.Project.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.Project.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NeedLogoutInterceptor implements HandlerInterceptor{
	
	private Rq rq;
	
	public NeedLogoutInterceptor(Rq rq) {
		this.rq = rq;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (rq.getLoginMemberNumber() != 0) {
			rq.jsReplace("해당 기능은 로그아웃 후 이용해주세요.", "/");
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}