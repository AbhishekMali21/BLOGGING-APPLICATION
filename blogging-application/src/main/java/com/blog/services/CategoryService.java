package com.blog.services;

import com.blog.payloads.CategoryDTO;

import java.util.List;

public interface CategoryService {

    // create
    public CategoryDTO createCategory(CategoryDTO categoryDTO);

    // update
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId);

    // delete
    public  void deleteCategory(Integer categoryId);

    // get
    public CategoryDTO getCategory(Integer categoryId);

    // get all
    public List<CategoryDTO> getCategories();
}
