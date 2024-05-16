package com.keduit.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
       //XMLHttpRequest에 x-requested-with 뜨면
        //ajax 비동기 통신의 경우 http request header에 xmlhttprequest라는 값을 넣어줌
        //이때 인증되지 않은 사용자가 ajax로 리소스 요청을 한 경우 unauthorized(401)에러를 발생시킴
        //나머지는 로그인을 유도

        if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED), "Unauthorized");
        }else{
            response.sendRedirect("/members/login");
        }
    }
}
