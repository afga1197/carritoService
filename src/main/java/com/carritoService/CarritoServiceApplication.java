package com.carritoService;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carritoService.model.ErrorInfo;

@SpringBootApplication
public class CarritoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarritoServiceApplication.class, args);
	}

	@Bean
	public ErrorInfo errorinfo() {
		return new ErrorInfo();
	}

}