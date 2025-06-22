package com.example.Gradely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GradelyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradelyApplication.class, args);
	}

}