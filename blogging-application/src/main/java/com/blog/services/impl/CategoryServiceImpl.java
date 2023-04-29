package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDTO;
import com.blog.repositories.CategoryRepository;
import com.blog.services.CategoryService;
import com.blog.utils.LoggingUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		LoggingUtils.logMethodStart();
		Category category = this.modelMapper.map(categoryDTO, Category.class);
		Category addedCategory = this.categoryRepository.save(category);
		log.info(category);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(addedCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {
		LoggingUtils.logMethodStart();
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		category.setCategoryTitle(categoryDTO.getCategoryTitle());
		category.setCategoryDesc(categoryDTO.getCategoryDesc());
		Category updatedCategory = this.categoryRepository.save(category);
		log.info(category);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(updatedCategory, CategoryDTO.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		LoggingUtils.logMethodStart();
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		this.categoryRepository.delete(category);
		log.info(category);
		LoggingUtils.logMethodEnd();
	}

	@Override
	public CategoryDTO getCategory(Integer categoryId) {
		LoggingUtils.logMethodStart();
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		log.info(category);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public List<CategoryDTO> getCategories() {
		LoggingUtils.logMethodStart();
		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDTO> categoryDTOS = categories.stream()
				.map(category -> this.modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
		log.info(categories);
		LoggingUtils.logMethodEnd();
		return categoryDTOS;
	}
}
