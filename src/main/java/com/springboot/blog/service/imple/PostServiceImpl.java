package com.springboot.blog.service.imple;

import com.springboot.blog.Dtos.PageResponse;
import com.springboot.blog.Dtos.PostDto;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.reposistory.CategoryRepository;
import com.springboot.blog.reposistory.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    private CategoryRepository categoryRepository;

    //injection using the constructor
    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper,CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //retrieve category by postDto
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category", "id", postDto.getCategoryId()));

        //convert Dto to Entity
        //we used private method name direct below we created method
        Post post= mapToEntity(postDto);

        //before saving we have to set category
        post.setCategory(category);

        Post newPost = postRepository.save(post);

        // now convert Entity to Dto because we need to return Dto
         PostDto postResponse =mapToDTO(newPost);
          return postResponse;
    }

    // Convert Entity into Dto
    private PostDto mapToDTO(Post post)
    {
        //now i used model mapper

        PostDto postDto=modelMapper.map(post,PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setDescription(post.getDescription());
//        postDto.setTitle(post.getTitle());
//        postDto.setContent(post.getContent());
        return postDto;
    }


    //convert Dto to entity
    private Post mapToEntity(PostDto postDto)
    {
          Post post =modelMapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
        return post;
    }

    @Override
    public PageResponse getAll(int pageNo,int pageSize,String sortBy,String sortDir) {

        //create Pageable instance from the Pageable class that are present in JpaRepos internally extends PageableSort interface
       // Pageable pageable= PageRequest.of(pageNo,pageSize);  //PageRequest class of() method to pass value
        //Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(sortBy).ascending());
        
        //now we implement sorting with help of sort class
        
        Sort sort= (sortDir.equalsIgnoreCase("ASC"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        
        //now we call PageRequest of method
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);


        Page<Post> posts = postRepository.findAll(pageable); //pageable return Page

        //get content for page object
        List<Post> ListOfPost = posts.getContent();

        //for Api page response
        List<PostDto> content = ListOfPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(content);
        pageResponse.setPageSize(posts.getSize());        //get all page and size From page class
        pageResponse.setPageNo(posts.getNumber());
        pageResponse.setTotalElements(posts.getNumberOfElements());
        pageResponse.setTotalPages(posts.getTotalPages());
        pageResponse.setLast(posts.isLast());
        return pageResponse;

    }

    //get post by id
    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("post","id",id));
        return mapToDTO(post);
    }

    //update post by id
    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

        //also want to update category as well so we need id of category as well

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category", "id", postDto.getCategoryId()));

        //update in post from the PostDto
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        //also update category
        post.setCategory(category);

        //now save the call the method
        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    //delete post by id from the database
    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
        postRepository.delete(post);
    }


    //get post by category
    @Override
    public List<PostDto> getPostByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
    }
}
