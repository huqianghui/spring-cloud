package com.seattle.msready.account.controller;

import com.seattle.msready.account.domain.User;
import com.seattle.msready.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = "/current", method = RequestMethod.GET)
    public User getCurrentAccount(@RequestParam("username") String username) {
        return userRepository.findByUsername(username);
    }
}
