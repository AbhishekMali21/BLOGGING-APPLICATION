package com.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;
import com.blog.payloads.StatusResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;
import com.blog.utils.LoggingUtils;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		LoggingUtils.logMethodStart();
		PostDTO post = this.postService.createPost(postDTO, userId, categoryId);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(post, HttpStatus.CREATED);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
		LoggingUtils.logMethodStart();
		PostResponse postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(postsByCategory);
	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
		LoggingUtils.logMethodStart();
		PostResponse postsByUser = this.postService.getPostsByUser(userId, pageNumber, pageSize);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(postsByUser);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId) {
		LoggingUtils.logMethodStart();
		PostDTO post = this.postService.getPostById(postId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(post);
	}

	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.POST_ID, required = false) String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ASC, required = false) String sortOrder) {
		LoggingUtils.logMethodStart();
		PostResponse allPosts = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(allPosts);
	}

	@DeleteMapping("/posts/{postId}")
	public StatusResponse deletePost(@PathVariable Integer postId) {
		LoggingUtils.logMethodStart();
		this.postService.deletePost(postId);
		LoggingUtils.logMethodEnd();
		return new StatusResponse("Post is successfully Deleted", true);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer postId) {
		LoggingUtils.logMethodStart();
		PostDTO updatePost = this.postService.updatePost(postDTO, postId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(updatePost);
	}

	@GetMapping("/posts/search")
	public ResponseEntity<List<PostDTO>> searchPostByTitle(
			@RequestParam(value = "titleQ", required = true) String titleQ) {
		LoggingUtils.logMethodStart();
		List<PostDTO> postDTOs = this.postService.searchPosts(titleQ);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(postDTOs);
	}

	@GetMapping("/posts/searchContent/{contentText}")
	public ResponseEntity<List<PostDTO>> searchPostByContent(@PathVariable String contentText) {
		LoggingUtils.logMethodStart();
		List<PostDTO> postDTOs = this.postService.searchPostsContentText(contentText);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(postDTOs);
	}

	// post image upload api
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<PostDTO> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException {
		LoggingUtils.logMethodStart();
		String fileName = this.fileService.uploadImage(path, image);
		PostDTO postDTO = this.postService.getPostById(postId);
		postDTO.setImageName(fileName);
		PostDTO updatePost = this.postService.updatePost(postDTO, postId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(updatePost);
	}

	// method to serve image files
	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {
		LoggingUtils.logMethodStart();
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		LoggingUtils.logMethodEnd();
	}
}
