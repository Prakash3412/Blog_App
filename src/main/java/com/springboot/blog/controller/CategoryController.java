package com.springboot.blog.controller;


import com.springboot.blog.Dtos.CategoryDto;
import com.springboot.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    //create addCategory rest api
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){

        CategoryDto savedCategory = categoryService.addCategory(categoryDto);

        return  new ResponseEntity<>(savedCategory,HttpStatus.CREATED);
    }


    //get  single category by id

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto>  getCategoryById(@PathVariable Long categoryId){
        CategoryDto category = categoryService.getCategory(categoryId);
         return  new ResponseEntity<>(category,HttpStatus.OK);
    }

    //get all categories

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        return  new ResponseEntity<>(allCategory,HttpStatus.OK);

    }

    //update category

    @PutMapping("{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId){
        CategoryDto updatedCategory = categoryService.updateCategoryById(categoryDto, categoryId);
         return  new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    //delete category by id

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable(value = "id") Long categoryId){
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>("categories deleted successfully",HttpStatus.OK);
    }
}
