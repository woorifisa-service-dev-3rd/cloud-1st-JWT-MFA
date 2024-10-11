package dev.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Cloud1stJwtMfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cloud1stJwtMfaApplication.class, args);
	}

}
