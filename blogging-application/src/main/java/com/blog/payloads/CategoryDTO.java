package com.blog.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

	private Integer categoryId;
	@NotBlank
	@Size(min = 4, message = "provide title with min of 4 chars")
	private String categoryTitle;
	@NotBlank
	@Size(min = 4, message = "provide description with min of 10 chars")
	private String categoryDesc;
}
