package com.blackhole.fiman;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FiManApplication {

	public static void main(String[] args) {
		//SpringApplication.run(FiManApplication.class, args);
		new SpringApplicationBuilder(FiManApplication.class)
			.listeners(new ApplicationEventListener())
			.run(args);
	}
	
	
}
