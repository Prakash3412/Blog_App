package com.springboot.blog.controller;

import com.springboot.blog.Dtos.PageResponse;
import com.springboot.blog.Dtos.PostDto;
import com.springboot.blog.reposistory.PostRepository;
import com.springboot.blog.service.PostService;

import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService; //using constructor based injection no need to write Autowired because one constructor
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create blog post

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PostDto> createPost( @Valid @RequestBody PostDto postDto)
  {
      PostDto post = postService.createPost(postDto);
      return new ResponseEntity<>(post, HttpStatus.CREATED);
  }

  //getAllPost Rest Apis
    @GetMapping
   public PageResponse getAllPosts(
//          @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
//          @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
//          @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
//          @RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir

            //now we doing same thing with avoid the hardcoding

            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    )
   {
       return postService.getAll(pageNo,pageSize,sortBy,sortDir);
   }

   //get single post by id
   @GetMapping("/{id}")
   public ResponseEntity<PostDto> getSinglePostById(@PathVariable long id)
   {
       PostDto postById = postService.getPostById(id);
       return new ResponseEntity<>(postById,HttpStatus.OK);
   }


   //update post by id
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<PostDto> updatePostById( @Valid @RequestBody PostDto postDto,@PathVariable long id)
   {
       PostDto response = postService.updatePost(postDto, id);
       return new ResponseEntity<>(response,HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<String> deletePost(@PathVariable long id)
   {
       postService.deletePost(id);
      return new ResponseEntity<>("Post entity Successfully deleted",HttpStatus.OK) ;
   }

   //get post by category id
    //apis/posts/category/1
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategory( @PathVariable Long categoryId){

        List<PostDto> posts = postService.getPostByCategory(categoryId);

        return  new ResponseEntity<>(posts,HttpStatus.OK);
    }
}
