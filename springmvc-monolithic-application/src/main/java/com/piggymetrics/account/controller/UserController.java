package com.piggymetrics.account.controller;

import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(path = "/byName", method = RequestMethod.GET)
	public User getUserByName(@RequestParam("username") String username) {
		LOGGER.debug("username :" + username);
		System.out.println("username:" + username);
		User dbUser= userRepository.findByUsername(username);
		System.out.println("dbUser:" + dbUser);
		return dbUser;
	}
}
