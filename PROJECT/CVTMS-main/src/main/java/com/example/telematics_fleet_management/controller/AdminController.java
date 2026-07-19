package com.example.telematics_fleet_management.controller;


import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {

        Map<String, Long> response = new HashMap<>();

        response.put("totalUsers", adminService.getTotalUsers());
        response.put("totalVehicles", adminService.getTotalVehicles());
        response.put("totalTrips", adminService.getTotalTrips());

        return response;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
}