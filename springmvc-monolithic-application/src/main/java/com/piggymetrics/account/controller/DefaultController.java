package com.piggymetrics.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String createNewAccount() {
        return "index.html";
    }
}
