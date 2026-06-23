package com.gymfit.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GymReservasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymReservasApiApplication.class, args);
	}
}