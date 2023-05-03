package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepository;
import com.blog.repositories.PostRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;
import com.blog.utils.LoggingUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
		LoggingUtils.logMethodStart();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		Post post = this.modelMapper.map(postDTO, Post.class);
		post.setImageName(AppConstants.DEFAULT_PNG);
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post addedPost = this.postRepository.save(post);
		log.info(post);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(addedPost, PostDTO.class);
	}

	/**
	 * @param postDTO
	 * @param postId
	 * @return
	 */
	@Override
	public PostDTO updatePost(PostDTO postDTO, Integer postId) {
		LoggingUtils.logMethodStart();
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		post.setTitle(postDTO.getTitle());
		post.setContent(postDTO.getContent());
		post.setImageName(postDTO.getImageName());
		Post updatedPost = this.postRepository.save(post);
		log.info(updatedPost);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(updatedPost, PostDTO.class);
	}

	/**
	 * @param postId
	 */
	@Override
	public void deletePost(Integer postId) {
		LoggingUtils.logMethodStart();
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		this.postRepository.delete(post);
		log.info(post);
		LoggingUtils.logMethodEnd();
	}

	/**
	 * @param postId
	 * @return
	 */
	@Override
	public PostDTO getPostById(Integer postId) {
		LoggingUtils.logMethodStart();
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));
		log.info(post);
		LoggingUtils.logMethodEnd();
		return this.modelMapper.map(post, PostDTO.class);
	}

	/**
	 * @return
	 */
	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		LoggingUtils.logMethodStart();
		Sort sort = null;
		if (sortOrder.equalsIgnoreCase(AppConstants.SORT_ASC)) {
			sort = Sort.by(sortBy).ascending();
		} else if (sortOrder.equalsIgnoreCase(AppConstants.SORT_DESC)) {
			sort = Sort.by(sortBy).descending();
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePosts = this.postRepository.findAll(pageable);
		List<Post> allPosts = pagePosts.getContent();
		List<PostDTO> postDTOS = allPosts.stream().map(post -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		PostResponse postResponse = paginationInfo(pagePosts, postDTOS);
		log.info(postResponse);
		LoggingUtils.logMethodEnd();
		return postResponse;
	}

	/**
	 * @param pagePosts
	 * @param postDTOS
	 * @return
	 */
	public PostResponse paginationInfo(Page<Post> pagePosts, List<PostDTO> postDTOS) {
		LoggingUtils.logMethodStart();
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDTOS);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());
		log.info(postResponse);
		LoggingUtils.logMethodEnd();
		return postResponse;
	}

	/**
	 * @param categoryId
	 * @return
	 */
	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
		LoggingUtils.logMethodStart();
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category Id", categoryId));
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> posts = this.postRepository.findByCategory(category, pageable);
		List<Post> allPosts = posts.getContent();
		List<PostDTO> postDTOS = allPosts.stream().map(post -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		PostResponse postResponse = paginationInfo(posts, postDTOS);
		log.info(postResponse);
		LoggingUtils.logMethodEnd();
		return postResponse;
	}

	/**
	 * @param userId
	 * @return
	 */
	@Override
	public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize) {
		LoggingUtils.logMethodStart();
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> posts = this.postRepository.findByUser(user, pageable);
		List<Post> allPosts = posts.getContent();
		List<PostDTO> postDTOS = allPosts.stream().map(post -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		PostResponse postResponse = paginationInfo(posts, postDTOS);
		log.info(postResponse);
		LoggingUtils.logMethodEnd();
		return postResponse;
	}

	/**
	 * @param keyword
	 * @return
	 */
	@Override
	public List<PostDTO> searchPosts(String keyword) {
		LoggingUtils.logMethodStart();
		List<Post> posts = this.postRepository.findByTitleContaining(keyword);
		List<PostDTO> postDTOs = posts.stream().map(post -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		log.info(postDTOs);
		LoggingUtils.logMethodEnd();
		return postDTOs;
	}

	/**
	 * @param contentText
	 * @return
	 */
	@Override
	public List<PostDTO> searchPostsContentText(String contentText) {
		LoggingUtils.logMethodStart();
		List<Post> posts = this.postRepository
				.searchByContentText(AppConstants.PERCENT_SIGN + contentText + AppConstants.PERCENT_SIGN);
		List<PostDTO> postDTOs = posts.stream().map(post -> this.modelMapper.map(post, PostDTO.class))
				.collect(Collectors.toList());
		log.info(postDTOs);
		LoggingUtils.logMethodEnd();
		return postDTOs;
	}
}
