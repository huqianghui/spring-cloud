package com.seattle.msready.auth.service.security;

import com.seattle.msready.auth.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service",path = "/users",configuration = FeignClientsConfiguration.class)
public interface UserServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/current", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	User getUserByName(@RequestParam("username") String username);

}
