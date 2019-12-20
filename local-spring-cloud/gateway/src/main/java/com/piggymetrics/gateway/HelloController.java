package com.piggymetrics.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("#{'${message:default}'}")
    private String message;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Spring Cloud's message:" + message;
    }
}
