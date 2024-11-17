package cz.jkdabing.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Welcome to admin info!";
    }
}
