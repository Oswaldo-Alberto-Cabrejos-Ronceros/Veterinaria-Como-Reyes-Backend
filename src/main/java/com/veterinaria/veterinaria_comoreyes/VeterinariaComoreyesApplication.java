package com.veterinaria.veterinaria_comoreyes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VeterinariaComoreyesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinariaComoreyesApplication.class, args);
	}
}
