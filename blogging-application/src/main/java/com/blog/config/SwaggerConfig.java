package com.blog.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo())
				.select()
	            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo getInfo() {
		return new ApiInfo("Blogging Application",
				"Welcome to the documentation for our blogging application! Here, you will find everything you need to know to start using our application's APIs.\n  Our application has been designed to provide you with all the necessary APIs to create, manage, and publish blog posts.",
				"1.0", "https://swagger.io/docs/specification/api-general-info/",
				new Contact("Abhishek S", "http://localhost:9090/swagger-ui/index.html#/", "root@gmail.com"), "License",
				"https://swagger.io/license/", Collections.emptyList());
	}
}
