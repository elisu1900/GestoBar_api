package com.elias.GestoBar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GestoBarApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestoBarApplication.class, args);
	}

}
