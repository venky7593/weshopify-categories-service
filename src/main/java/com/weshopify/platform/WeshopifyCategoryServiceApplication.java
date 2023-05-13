package com.weshopify.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weshopify.platform.repo.CategoriesRepository;

@SpringBootApplication
public class WeshopifyCategoryServiceApplication {

	@Autowired
	private CategoriesRepository catRepo;

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyCategoryServiceApplication.class, args);
	}

}
