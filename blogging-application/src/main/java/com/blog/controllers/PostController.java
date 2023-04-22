package com.blog.controllers;

import com.blog.config.AppConstants;
import com.blog.services.FileService;
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

import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;
import com.blog.payloads.StatusResponse;
import com.blog.services.PostService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
		PostDTO post = this.postService.createPost(postDTO, userId, categoryId);
		return new ResponseEntity<PostDTO>(post, HttpStatus.CREATED);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
		PostResponse postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsByCategory, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
		PostResponse postsByUser = this.postService.getPostsByUser(userId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsByUser, HttpStatus.OK);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Integer postId) {
		return ResponseEntity.ok(this.postService.getPostById(postId));
	}

	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.POST_ID, required = false) String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ASC, required = false) String sortOrder) {
		return ResponseEntity.ok(this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder));
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

	@GetMapping("/posts/search")
	public ResponseEntity<List<PostDTO>> searchPostByTitle(@RequestParam(value = "titleQ", required = true) String titleQ){
		List<PostDTO> postDTOs = this.postService.searchPosts(titleQ);
		return new ResponseEntity<List<PostDTO>>(postDTOs, HttpStatus.OK);
	}

	@GetMapping("/posts/searchContent/{contentText}")
	public ResponseEntity<List<PostDTO>> searchPostByContent(@PathVariable String contentText){
		List<PostDTO> postDTOs = this.postService.searchPostsContentText(contentText);
		return new ResponseEntity<List<PostDTO>>(postDTOs, HttpStatus.OK);
	}

	// post image upload api
	@PostMapping("/posts/image/upload/{postId}")
	public ResponseEntity<PostDTO> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId) throws IOException {
		String fileName = this.fileService.uploadImage(path, image);
		PostDTO postDTO = this.postService.getPostById(postId);
		postDTO.setImageName(fileName);
		PostDTO updatePost = this.postService.updatePost(postDTO, postId);
		return new ResponseEntity<>(updatePost, HttpStatus.OK);
	}

	// method to serve image files
	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
