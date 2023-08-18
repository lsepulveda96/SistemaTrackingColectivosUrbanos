package com.stcu;

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
public class StcuApplication  {

	public static void main(String[] args) {
		SpringApplication.run(StcuApplication.class, args);
	}

    @Bean
    WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:4200","http://localhost:50004/stcu2")
					.allowedMethods("GET","POST","PUT","DELETE");
			}
		};
	}
}
