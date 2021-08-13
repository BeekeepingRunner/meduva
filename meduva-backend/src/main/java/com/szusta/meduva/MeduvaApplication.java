package com.szusta.meduva;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@SpringBootApplication
public class MeduvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeduvaApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			if (userService.getUserByLogin("admin") == null) {
				userService.saveUser(
						new User(
								null,
								"john",
								"doe",
								"samplemail@mail.com",
								"48123123123",
								"admin",
								"admin",
								null,
								false,
								null,
								new ArrayList<Role>()));

				userService.addRoleToUser("admin", "ROLE_CLIENT");
				userService.addRoleToUser("admin", "ROLE_WORKER");
				userService.addRoleToUser("admin", "ROLE_RECEPTIONIST");
				userService.addRoleToUser("admin", "ROLE_ADMIN");
			}
		};
	}
}
