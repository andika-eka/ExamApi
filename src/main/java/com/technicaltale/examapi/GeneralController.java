package com.technicaltale.examapi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {
    @GetMapping("/test")
    public String sayHello() {
        return "Hello World - Technical Tale is Online!";
    }
}
