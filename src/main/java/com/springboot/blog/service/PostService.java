package com.springboot.blog.service;

import com.springboot.blog.Dtos.PageResponse;
import com.springboot.blog.Dtos.PostDto;

import java.util.List;

public interface PostService {
    //create post abstract method
    PostDto createPost(PostDto postDto);

    PageResponse getAll(int pageNo, int pageSize,String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto ,long id);

    void deletePost(long id);

    //get ALL post by Category id
    List<PostDto> getPostByCategory(Long categoryId);

}
