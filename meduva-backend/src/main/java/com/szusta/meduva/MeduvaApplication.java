package com.szusta.meduva;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@SpringBootApplication
public class MeduvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeduvaApplication.class, args);
	}

	/*
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
*/

	/*
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveUser(
					new User(
							null,
							"name",
							"surname",
							"samplemail@mail.com",
							"48123123123",
							"sampleLogin",
							"1234",
							null,
							false,
							null,
							new ArrayList<Role>()));

			userService.addRoleToUser("sampleLogin", "ROLE_CLIENT");
			userService.addRoleToUser("sampleLogin", "ROLE_WORKER");
		};
	}

	 */
}
