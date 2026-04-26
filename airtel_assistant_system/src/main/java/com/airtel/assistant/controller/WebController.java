package com.airtel.assistant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String landingPage() {
        return "login"; // This looks for src/main/resources/templates/login.html
    }
}