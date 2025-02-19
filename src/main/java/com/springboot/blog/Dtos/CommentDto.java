package com.springboot.blog.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private  long id;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "email should be not be null or empty")
    @Email(message = "email should  be in format")
    private String email;

    @NotEmpty(message = "body should be not be null or empty")
    @Size(min = 10,message = "body should be 10 character")
    private String body;
}
