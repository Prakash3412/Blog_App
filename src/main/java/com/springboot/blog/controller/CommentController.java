package com.springboot.blog.controller;


import com.springboot.blog.Dtos.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    //injecting the commentService

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //to create comment resource by post id

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment( @PathVariable long postId,
                                                     @Valid @RequestBody CommentDto commentDto){
        CommentDto comment = commentService.createComment(postId, commentDto);
        return  new ResponseEntity<>(comment, HttpStatus.CREATED);

    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable long postId){
        List<CommentDto> allCommentsByPostId = commentService.getAllCommentsByPostId(postId);
             return  new ResponseEntity<>(allCommentsByPostId,HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId){
        CommentDto singleCommentById = commentService.getSingleCommentById(postId, commentId);
         return  new ResponseEntity<>(singleCommentById,HttpStatus.OK);
    }


    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long postId,
                                                    @PathVariable long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
         return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }


    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById( @PathVariable Long postId, @PathVariable long commentId){
            commentService.deleteComment(postId, commentId);
            return  new ResponseEntity<>("Comment successfully deleted",HttpStatus.OK);
    }

}
