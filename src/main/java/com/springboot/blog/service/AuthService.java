package com.springboot.blog.service;

import com.springboot.blog.Dtos.LoginDto;
import com.springboot.blog.Dtos.RegisterDto;

public interface AuthService {

    //for login that are already present in database
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
