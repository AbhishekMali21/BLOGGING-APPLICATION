package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.StatusResponse;
import com.blog.payloads.UserDTO;
import com.blog.services.UserService;
import com.blog.utils.LoggingUtils;

@RestController
@RequestMapping("/api/blog/users")
public class UserController {
	@Autowired
	private UserService userService;

	// GET
	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		LoggingUtils.logMethodStart();
		List<UserDTO> allUsers = this.userService.getAllUsers();
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(allUsers);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId) {
		LoggingUtils.logMethodStart();
		UserDTO user = this.userService.getUserById(userId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(user);
	}

	// POST
	@PostMapping("/")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
		LoggingUtils.logMethodStart();
		UserDTO createUserDTO = this.userService.createUser(userDTO);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
	}

	// PUT
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Integer userId) {
		LoggingUtils.logMethodStart();
		UserDTO updateUser = this.userService.updateUser(userDTO, userId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(updateUser);
	}

	// DELETE
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<StatusResponse> deleteUser(@PathVariable Integer userId) {
		LoggingUtils.logMethodStart();
		this.userService.deleteUser(userId);
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(new StatusResponse("User Deleted Successfully", true));
	}

}
