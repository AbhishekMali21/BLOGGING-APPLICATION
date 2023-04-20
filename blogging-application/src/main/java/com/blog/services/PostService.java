package com.blog.services;

import java.util.List;

import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;

public interface PostService {

	// create
	PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId);

	// update
	PostDTO updatePost(PostDTO postDTO, Integer postId);

	// delete
	void deletePost(Integer postId);

	// get single post
	PostDTO getPostById(Integer postId);

	// get all posts
	PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	// get all posts by category
	PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize);

	// get all posts by user
	PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize);

	// search posts
	List<PostDTO> searchPosts(String keyword);
}
