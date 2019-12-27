package com.piggymetrics.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableOAuth2Client
public class AccountMonolithicApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountMonolithicApplication.class, args);
	}

}
