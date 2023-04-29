package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.CommentDTO;
import com.blog.payloads.StatusResponse;
import com.blog.services.CommentService;
import com.blog.utils.LoggingUtils;

@RestController
@RequestMapping("/api/blog")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/comments/post/{postId}")
	public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO, @PathVariable Integer postId) {
		LoggingUtils.logMethodStart();
		CommentDTO newCommentDTO = this.commentService.createComment(commentDTO, postId);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(newCommentDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<StatusResponse> deleteComment(@PathVariable Integer commentId) {
		LoggingUtils.logMethodStart();
		this.commentService.deleteComment(commentId);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(new StatusResponse("Comment Deleted Successfully !!", true), HttpStatus.OK);
	}
}
