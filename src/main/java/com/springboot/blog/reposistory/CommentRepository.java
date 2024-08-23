package com.springboot.blog.reposistory;

import com.springboot.blog.Dtos.CommentDto;
import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //for get ALL comments by postId we can write standard format like this instead fo fetching from manually
    List<CommentDto> findByPostId(long postId);
}
