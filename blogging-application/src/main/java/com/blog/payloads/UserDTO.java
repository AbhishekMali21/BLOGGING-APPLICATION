package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	private int id;

	@NotEmpty
	@Size(min = 4, message = "name must be min of 4 characters")
	private String name;
	@NotEmpty
	@Email(message = "email address is not valid")
	private String email;
	@NotEmpty
	@Size(min = 8, max = 20, message = "password must be min of 8 and max of 20 characters")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "password must be at least 8 characters long and contain both letters and numbers")
	private String password;
	@NotEmpty
	private String about;

	// to ignore the password from the json response as it is password encoded
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	private Set<RoleDTO> role = new HashSet<>();

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", email=" + email + ", about=" + about + ", role=" + role
				+ "]";
	}

}
