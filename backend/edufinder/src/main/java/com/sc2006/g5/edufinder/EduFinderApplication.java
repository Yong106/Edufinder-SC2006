package com.sc2006.g5.edufinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EduFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduFinderApplication.class, args);
	}

}
