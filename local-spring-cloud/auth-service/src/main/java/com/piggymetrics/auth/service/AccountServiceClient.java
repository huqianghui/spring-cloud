package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service",path = "/accounts/users")
public interface AccountServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/byName", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	User getUserByName(@RequestParam("username") String username);

}
