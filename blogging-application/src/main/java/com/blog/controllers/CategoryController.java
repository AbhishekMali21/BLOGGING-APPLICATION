package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.CategoryDTO;
import com.blog.payloads.StatusResponse;
import com.blog.services.CategoryService;
import com.blog.utils.LoggingUtils;

@RestController
@RequestMapping("/api/blog/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// create
	@PostMapping("/")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		LoggingUtils.logMethodStart();
		CategoryDTO createCategoryDTO = this.categoryService.createCategory(categoryDTO);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(createCategoryDTO, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			@PathVariable Integer categoryId) {
		LoggingUtils.logMethodStart();
		CategoryDTO updateCategoryDTO = this.categoryService.updateCategory(categoryDTO, categoryId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(updateCategoryDTO);
	}

	// delete
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<StatusResponse> deleteCategory(@PathVariable Integer categoryId) {
		LoggingUtils.logMethodStart();
		this.categoryService.deleteCategory(categoryId);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(new StatusResponse("Category is deleted successfully !!", true), HttpStatus.OK);
	}

	// get
	@GetMapping("/")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		LoggingUtils.logMethodStart();
		List<CategoryDTO> categories = this.categoryService.getCategories();
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(categories);
	}

	// get all
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer categoryId) {
		LoggingUtils.logMethodStart();
		CategoryDTO category = this.categoryService.getCategory(categoryId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(category);
	}

}
