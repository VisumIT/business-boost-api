package com.visumIT.Business.boost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.viumIT.business.boost.upload.FirebaseStorageService;

@SpringBootApplication
public class BusinessBoostApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessBoostApplication.class, args);
	}
	@Bean
	public FirebaseStorageService init() {
		return new FirebaseStorageService();
	}
	
	

}
