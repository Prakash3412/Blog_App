package com.springboot.blog.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorDetails {

    private Date timeStamp;
    private String message;

    private String details;

    private HttpStatus httpStatus;


}
