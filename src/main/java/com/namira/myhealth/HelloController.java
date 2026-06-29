package com.namira.myhealth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "MyHealth API is running!";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Namira! Your server is working.";
    }
}