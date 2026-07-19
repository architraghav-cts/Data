package com.example.telematics_fleet_management.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VehicleServiceViewController {
    @GetMapping("/service/dashboard")
    public String dashboard() {
        return "service/dashboard";
    }
}

