package com.example.telematics_fleet_management.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TripViewController {
    @GetMapping("/trip/dashboard")
    public String dashboard() {
        return "trip/dashboard";
    }
}
