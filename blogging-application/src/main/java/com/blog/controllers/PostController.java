package com.blog.controllers;

import java.util.List;

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
	public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Integer categoryId) {
		List<PostDTO> posts = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDTO>>(posts, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Integer userId) {
		List<PostDTO> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDTO>>(posts, HttpStatus.OK);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId) {
		return ResponseEntity.ok(this.postService.getPostById(postId));
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostDTO>> getAllPosts(
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
