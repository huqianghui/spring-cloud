package com.piggymetrics.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringCloudApplication
@EnableHystrixDashboard
public class HystrixMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixMonitoringApplication.class, args);
	}
}
