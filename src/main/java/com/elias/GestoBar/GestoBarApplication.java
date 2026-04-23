package com.elias.GestoBar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GestoBarApplication {

	/**public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// Añade aquí las contraseñas que quieras hashear
		String[] passwords = {
				"admin123",
				"camarero2024",
				"gestobar"
		};

		for (String raw : passwords) {
			System.out.printf("%-20s -> %s%n", raw, encoder.encode(raw));
		}
	}**/
	public static void main(String[] args) {
		SpringApplication.run(GestoBarApplication.class, args);
	}

}
