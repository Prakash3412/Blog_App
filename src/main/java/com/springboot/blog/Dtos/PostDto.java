package com.springboot.blog.Dtos;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private long id;
     //now we are going to valid the Dto using jsr 380 framework


    //post title should not be null or empty
    //post title should have atLeast two character

    @NotEmpty
    @Size(min = 2,message = "Post Title should have at least two character")
    private String title;

    @NotEmpty
    @Size(min = 10,message = "Post description should have min 10 character")
    private String description;
    @NotEmpty
    private String content;

    private Set<CommentDto> comments;

    //to add category user pass the category as well we already mapped to each other

    private Long categoryId;
}
