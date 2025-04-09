package org.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class BookVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookVaultApplication.class, args);
		System.out.println("✅ BookVault Application Started with Method Security Enabled");
	}

	@PostConstruct
	public void init() {
		System.out.println("✅ Method-level security (PreAuthorize/PostAuthorize) is active");
	}
}
