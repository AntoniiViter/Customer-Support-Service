package com.example.customersupport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @GetMapping("/registration")
    public String registration() {
        //TODO
        return "TODO: Registration page";
    }
}
