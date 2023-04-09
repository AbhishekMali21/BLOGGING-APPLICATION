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
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.StatusResponse;
import com.blog.payloads.UserDTO;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/blog/users")
public class UserController {
	@Autowired
	private UserService userService;

	// GET
	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getSingleUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

	// POST
	@PostMapping("/")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		UserDTO createUserDTO = this.userService.createUser(userDTO);
		return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
	}

	// PUT
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Integer userId) {
		UserDTO updateUser = this.userService.updateUser(userDTO, userId);
		return ResponseEntity.ok(updateUser);
	}

	// DELETE
	@DeleteMapping("/{userId}")
	public ResponseEntity<StatusResponse> deleteUser(@PathVariable Integer userId) {
		this.userService.deleteUser(userId);
		return ResponseEntity.ok(new StatusResponse("User Deleted Successfully", true));
	}

}
