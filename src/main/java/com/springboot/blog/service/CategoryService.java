package com.springboot.blog.service;

import com.springboot.blog.Dtos.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(Long categoryId);

    List<CategoryDto> getAllCategory();

    CategoryDto updateCategoryById(CategoryDto categoryDto,Long categoryId);

    void deleteCategory(Long categoryId);
}
