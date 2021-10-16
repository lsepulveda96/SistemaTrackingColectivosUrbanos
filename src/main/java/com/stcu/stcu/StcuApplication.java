package com.stcu.stcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = {"com.stcu","com.stcu.repository"}, exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories("com.stcu.repository")
@EntityScan("com.stcu.model")
public class StcuApplication {

	public static void main(String[] args) {
		SpringApplication.run(StcuApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:4200")
					.allowedMethods("GET","POST","PUT","DELETE");
				

				/*
				registry.addMapping("/usuarios").allowedOrigins("http://localhost:4200");

				registry.addMapping("/colectivos").allowedOrigins("http://localhost:4200");
				registry.addMapping("/colectivo/{id}").allowedOrigins( "http://localhost:4200");

				registry.addMapping("/lineas").allowedOrigins("http://localhost:4200");
				registry.addMapping("/paradas").allowedOrigins("http://localhost:4200");
				*/
			}
		};
	}
}
