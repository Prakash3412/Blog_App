package com.springboot.blog.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//when unauthorized person try to access then it will through
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //to give the response error unauthorized person with the help of commence method that present in AuthenticationEntryPoint interface
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

         //when unauthorized person try to access then it will through

          response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());

    }
}
