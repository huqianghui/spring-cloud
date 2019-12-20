package com.piggymetrics.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
@EnableDiscoveryClient
public class TurbineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurbineServiceApplication.class, args);
	}
}
