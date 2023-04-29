package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exceptions.AuthorizationException;
import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.payloads.UserDTO;
import com.blog.security.JwtTokenHelper;
import com.blog.services.UserService;
import com.blog.utils.ApiPerformanceUtil;
import com.blog.utils.LoggingUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest)
			throws AuthorizationException {
		LoggingUtils.logMethodStart();
		JwtAuthResponse response = new JwtAuthResponse();
		ApiPerformanceUtil.measure(() -> {
			this.authenticate(jwtAuthRequest.getUserName(), jwtAuthRequest.getPassword());
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUserName());
			String generateToken = this.jwtTokenHelper.generateToken(userDetails);
			response.setToken(generateToken);
		}, "createToken API");
		LoggingUtils.logMethodEnd();
		return ResponseEntity.ok(response);
	}

	private void authenticate(String userName, String password) throws AuthorizationException {
		LoggingUtils.logMethodStart();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userName, password);
		try {
			this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (BadCredentialsException e) {
			log.info("Invalid username or password !!");
			throw new AuthorizationException("Invalid username or password !!");
		}
		LoggingUtils.logMethodEnd();
	}

	// register new user via api
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
		LoggingUtils.logMethodStart();
		UserDTO registeredUser = this.userService.registerNewUser(userDTO);
		LoggingUtils.logMethodEnd();
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}
}
