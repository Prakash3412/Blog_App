package com.springboot.blog.service;

import com.springboot.blog.Dtos.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId,CommentDto commentDto);

    List<CommentDto> getAllCommentsByPostId(long postId);

    CommentDto getSingleCommentById(Long postId,Long commentId);

    CommentDto updateComment(Long postId,long commentId,CommentDto commentRequest);

    void deleteComment(Long postId,long commentId);
}
