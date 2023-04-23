package com.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CommentDTO;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.PostRepository;
import com.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDTO createComment(CommentDTO commentDTO, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		Comment comment = this.modelMapper.map(commentDTO, Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepository.save(comment);
		return this.modelMapper.map(savedComment, CommentDTO.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment Id", commentId));
		this.commentRepository.delete(comment);
	}

}
