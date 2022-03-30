package com.openclassrooms.safetynetalertsP5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetynetalertsP5Application {

	public static void main(String[] args) {
		LoadDataJSON.loadData();
		SpringApplication.run(SafetynetalertsP5Application.class, args);
	}

}
