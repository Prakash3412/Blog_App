package com.springboot.blog.service.imple;

import com.springboot.blog.Dtos.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.reposistory.CommentRepository;
import com.springboot.blog.reposistory.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {


    //auto wiring both repository
    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
        this.modelMapper=modelMapper;
    }


    //to create comment resource by post id
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
       //change to dto to entity
        Comment comment = mapToEntity(commentDto);

        //need to retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));

        //set post to comment entity
        comment.setPost(post);

        //now save the comment into db
        Comment save = commentRepository.save(comment);

        return mapToDto(save);
    }


    //get all comment by post id
    @Override
    public List<CommentDto> getAllCommentsByPostId(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        //retrieve comment by post id
        List<Comment> comments = post.getComments();

        //convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    //get comment by id that if it  belongs to post with id=postId
    @Override
    public CommentDto getSingleCommentById(Long postId, Long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "is", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));

        //checking comment belongs to post or not
        if(comment.getPost().getId()!= post.getId()){
             throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"comment doesn't belongs to post");
        }
        return mapToDto(comment);
    }


    //update comment by id if it belongs to post with id = postId
    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "is", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));

        //checking comment belongs to post or not
        if(comment.getPost().getId()!= post.getId()){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"comment doesn't belongs to post");
        }

        //update the comment entity

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());

        //save the updated value
        Comment updatedComment = commentRepository.save(comment);

        //return Dto change to entity to dto
        return mapToDto(updatedComment);
    }



    //delete comment by id if it belongs to post with id = postId

    @Override
    public void deleteComment(Long postId, long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "is", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));
      //checking comment belongs to post or not
        if(comment.getPost().getId()!= post.getId()){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"comment doesn't belongs to post");
        }

        //delete the comment

        commentRepository.delete(comment);
    }


    //change Comment Entity to Comment Dto
    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto= modelMapper.map(comment,CommentDto.class);

//          commentDto.setId(comment.getId());
//          commentDto.setName(comment.getName());
//          commentDto.setBody(comment.getBody());
//          commentDto.setEmail(comment.getEmail());

          return commentDto;
    }

    //change Comment Entity to CommentDto

     public Comment mapToEntity(CommentDto commentDto){
           Comment comment = modelMapper.map(commentDto,Comment.class);

//           comment.setId(commentDto.getId());
//           comment.setBody(commentDto.getBody());
//           comment.setName(commentDto.getName());
//           comment.setEmail(commentDto.getEmail());
           return comment;
     }
}
