package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDTO;
import com.blog.repositories.CategoryRepository;
import com.blog.repositories.PostRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * @param postDTO
	 * @return
	 */
	@Override
	public PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		Post post = this.modelMapper.map(postDTO, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post addedPost = this.postRepository.save(post);
		return this.modelMapper.map(addedPost, PostDTO.class);
	}

	/**
	 * @param postDTO
	 * @param postId
	 * @return
	 */
	@Override
	public PostDTO updatePost(PostDTO postDTO, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		post.setTitle(postDTO.getTitle());
		post.setContent(postDTO.getContent());
		post.setImageName(postDTO.getImageName());
		Post updatedPost = this.postRepository.save(post);
		return this.modelMapper.map(updatedPost, PostDTO.class);
	}

	/**
	 * @param postId
	 */
	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		this.postRepository.delete(post);
	}

	/**
	 * @param postId
	 * @return
	 */
	@Override
	public PostDTO getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		return this.modelMapper.map(post, PostDTO.class);
	}

	/**
	 * @return
	 */
	@Override
	public List<PostDTO> getAllPosts(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> pagePosts = this.postRepository.findAll(pageable);
		List<Post> allPosts = pagePosts.getContent();
		List<PostDTO> postDTOS = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		return postDTOS;
	}

	/**
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<PostDTO> getPostsByCategory(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category Id", categoryId));
		List<Post> posts = this.postRepository.findByCategory(category);
		List<PostDTO> postDTOS = posts.stream().map((post) -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		return postDTOS;
	}

	/**
	 * @param userId
	 * @return
	 */
	@Override
	public List<PostDTO> getPostsByUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		List<Post> posts = this.postRepository.findByUser(user);
		List<PostDTO> postDTOS = posts.stream().map((post) -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		return postDTOS;
	}

	/**
	 * @param keyword
	 * @return
	 */
	@Override
	public List<PostDTO> searchPosts(String keyword) {
		return null;
	}
}
