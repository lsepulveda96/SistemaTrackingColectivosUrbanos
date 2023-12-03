package com.stcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import jakarta.annotation.Resource;
import javax.annotation.Resource;

import com.stcu.services.FileStorageService;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication(scanBasePackages = { "com.stcu", "com.stcu.repository" }, exclude = {
		SecurityAutoConfiguration.class })
@EnableJpaRepositories("com.stcu.repository")
@EntityScan("com.stcu.model")
public class StcuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StcuApplication.class, args);
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(
								"http://localhost:4200",
								"http://localhost:50004/stcu2",
								"http://localhost:50004/stcu2service")
						.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}

	@Resource
	FileStorageService storageService;

	@Override
	public void run(String... arg) throws Exception {
		// storageService.deleteAll();
		storageService.init();
	}
}
