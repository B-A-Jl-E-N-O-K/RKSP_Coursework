package com.example.rksp_coursework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EntityScan("com.example.rksp_coursework*")
@SpringBootApplication
@EnableScheduling
public class RkspCourseworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RkspCourseworkApplication.class, args);
	}

}
