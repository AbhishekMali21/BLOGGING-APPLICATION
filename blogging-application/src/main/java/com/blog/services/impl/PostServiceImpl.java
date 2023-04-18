package com.blog.services.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
		return null;
	}

	/**
	 * @param postId
	 */
	@Override
	public void deletePost(Integer postId) {

	}

	/**
	 * @param postId
	 * @return
	 */
	@Override
	public PostDTO getPostById(Integer postId) {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public List<PostDTO> getAllPosts() {
		return null;
	}

	/**
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<PostDTO> getPostsByCategory(Integer categoryId) {
		return null;
	}

	/**
	 * @param userId
	 * @return
	 */
	@Override
	public List<PostDTO> getPostsByUser(Integer userId) {
		return null;
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
