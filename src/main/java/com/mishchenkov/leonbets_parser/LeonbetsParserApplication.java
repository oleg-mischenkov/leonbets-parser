package com.mishchenkov.leonbets_parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LeonbetsParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeonbetsParserApplication.class, args);
	}

}
