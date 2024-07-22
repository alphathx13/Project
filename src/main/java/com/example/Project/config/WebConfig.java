package com.example.Project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.Project.interceptor.BeforeActionInterceptor;
import com.example.Project.interceptor.NeedLoginInterceptor;
import com.example.Project.interceptor.NeedLogoutInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private BeforeActionInterceptor beforeActionInterceptor;
	private NeedLoginInterceptor needLoginInterceptor;
	private NeedLogoutInterceptor needLogoutInterceptor;

	public WebConfig(BeforeActionInterceptor beforeActionInterceptor, NeedLoginInterceptor needLoginInterceptor, NeedLogoutInterceptor needLogoutInterceptor) {
		this.beforeActionInterceptor = beforeActionInterceptor;
		this.needLoginInterceptor = needLoginInterceptor;
		this.needLogoutInterceptor = needLogoutInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");
		// "/**" -> 모든 요청
		
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/user/member/config")
				.addPathPatterns("/user/member/myPage").addPathPatterns("/user/member/withdrawal");
		
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/user/member/join")
		.addPathPatterns("/user/member/login");
	}
	
	
}
