package com.bolt.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HelloworldApplication {

    @Value("${TARGET:NOT SPECIFIED}")
    String target;

    @RestController
    class HelloworldController {
        @GetMapping("/")
        String hello() {
            return "Hello World: " + target;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }
}