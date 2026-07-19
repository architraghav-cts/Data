package com.example.telematics_fleet_management.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fleet")
public class FleetManagerViewController {

    @GetMapping({"/register-vehicle", "/vehicles/register"})
    public String showVehicleRegistrationPage() {
        return "fleet/register-vehicle";
    }
}