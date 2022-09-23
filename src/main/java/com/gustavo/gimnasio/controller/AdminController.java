package com.gustavo.gimnasio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/home")
    public String adminHome() {
        return "admin home";
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin home";
    }
}
