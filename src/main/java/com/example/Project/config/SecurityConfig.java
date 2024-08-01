//package com.example.Project.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//        	.authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests
//                    .requestMatchers("/", "/user/home/main", "/common/error").permitAll()  // 공개 URL 설정
////                    .anyRequest().authenticated()           // 나머지 요청은 인증 필요
//            ) 
//            .formLogin(formLogin ->
//                formLogin
//                    .loginPage("/login")   // 로그인 페이지 설정
//                    .permitAll()           // 로그인 페이지는 누구나 접근 가능
//            )
//            .logout(logout ->
//                logout
//                    .permitAll()           // 로그아웃은 누구나 접근 가능
//            );
//
//        return http.build();
//    }
//}