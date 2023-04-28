/**
 * 
 */
package com.blog.services;

import java.util.List;

import com.blog.payloads.UserDTO;

/**
 * @author ABHISHEK
 *
 */
public interface UserService {

	UserDTO registerNewUser(UserDTO userDTO);

	UserDTO createUser(UserDTO user);

	UserDTO updateUser(UserDTO user, Integer userId);

	UserDTO getUserById(Integer userId);

	List<UserDTO> getAllUsers();

	void deleteUser(Integer userId);
}
