package com.springboot.blog.controller;

import com.springboot.blog.Dtos.JwtAuthResponse;
import com.springboot.blog.Dtos.LoginDto;
import com.springboot.blog.Dtos.RegisterDto;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //create Rest Api for login

    @PostMapping(value = {"/login" ,"/signin"})
    public ResponseEntity<JwtAuthResponse> login( @RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

         jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    //build register Rest api user will register

    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String registerResponse = authService.register(registerDto);
        return  new ResponseEntity<>(registerResponse,HttpStatus.CREATED);
    }
}
