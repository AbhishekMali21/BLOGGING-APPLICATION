package com.blog.payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {

	private String userName;
	private String password;
}
