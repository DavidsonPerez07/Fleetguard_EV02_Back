package com.microservice.fleetLocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.microservice.fleetLocation")
@EnableScheduling
public class MicroserviceFleetLocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceFleetLocationApplication.class, args);
	}

}
