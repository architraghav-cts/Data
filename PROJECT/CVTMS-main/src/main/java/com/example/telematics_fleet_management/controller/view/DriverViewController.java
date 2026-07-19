package com.example.telematics_fleet_management.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/driver")
public class DriverViewController {
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "driver/dashboard";
    }

}