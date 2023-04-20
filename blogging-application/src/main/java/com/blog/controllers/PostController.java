package com.blog.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;
import com.blog.payloads.StatusResponse;
import com.blog.services.PostService;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDTO post = this.postService.createPost(postDTO, userId, categoryId);
		return new ResponseEntity<PostDTO>(post, HttpStatus.CREATED);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PostResponse postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsByCategory, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		PostResponse postsByUser = this.postService.getPostsByUser(userId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsByUser, HttpStatus.OK);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId) {
		return ResponseEntity.ok(this.postService.getPostById(postId));
	}

	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		return ResponseEntity.ok(this.postService.getAllPosts(pageNumber, pageSize));
	}

	@DeleteMapping("/posts/{postId}")
	public StatusResponse deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new StatusResponse("Post is successfully Deleted", true);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer postId) {
		PostDTO updatePost = this.postService.updatePost(postDTO, postId);
		return new ResponseEntity<PostDTO>(updatePost, HttpStatus.OK);
	}
}
