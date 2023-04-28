package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.UserDTO;
import com.blog.repositories.RoleRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDTO createUser(UserDTO userDTO) {
		User user = this.dtoToUser(userDTO);
		this.userRepository.save(user);
		return null;
	}

	@Override
	public UserDTO updateUser(UserDTO userDTO, Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow((() -> new ResourceNotFoundException("User", " id ", userId)));
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setAbout(user.getAbout());
		User updatedUser = this.userRepository.save(user);
		UserDTO userDTO1 = this.userToDTO(updatedUser);
		return userDTO1;
	}

	@Override
	public UserDTO getUserById(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		return this.userToDTO(user);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = this.userRepository.findAll();
		List<UserDTO> userDTOs = users.stream().map(user -> this.userToDTO(user)).collect(Collectors.toList());
		return userDTOs;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		this.userRepository.delete(user);
	}

	public User dtoToUser(UserDTO userDTO) {
		User user = this.modelMapper.map(userDTO, User.class);
//		User user = new User();
//		user.setId(userDTO.getId());
//		user.setName(userDTO.getName());
//		user.setEmail(userDTO.getEmail());
//		user.setAbout(userDTO.getAbout());
//		user.setPassword(userDTO.getPassword());
		return user;
	}

	public UserDTO userToDTO(User user) {
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
//		UserDTO userDTO = new UserDTO();
//		userDTO.setId(user.getId());
//		userDTO.setName(user.getName());
//		userDTO.setEmail(user.getEmail());
//		userDTO.setAbout(user.getAbout());
//		userDTO.setPassword(user.getPassword());
		return userDTO;
	}

	@Override
	public UserDTO registerNewUser(UserDTO userDTO) {
		User user = this.modelMapper.map(userDTO, User.class);
		// encode the password
		if (user.getPassword() == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		// roles
		Role role = this.roleRepository.findById(AppConstants.ROLE_NORMAL_USER).get();
		user.getRole().add(role);
		User newUser = this.userRepository.save(user);
		return this.modelMapper.map(newUser, UserDTO.class);
	}

}
