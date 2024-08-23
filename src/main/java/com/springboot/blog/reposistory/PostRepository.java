package com.springboot.blog.reposistory;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    //get All posts by Category id

    List<Post> findByCategoryId(Long categoryId);

}
