package com.example.telematics_fleet_management.controller;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.model.Trip;
import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.service.DriverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> response = new HashMap<>();

        response.put("driver", driverService.getDriver());
        response.put("totalTrips", driverService.getTotalTrips());
        response.put("totalDistance", driverService.getTotalDistance());

        return response;
    }
    @GetMapping("/recent-trips")
    public List<Trip> getRecentTrips() {
        return driverService.getRecentTrips();
    }

    @GetMapping("/trips")
    public List<Trip> getTrips() {
        return driverService.getRecentTrips();
    }

    @GetMapping("/scores")
    public List<DriverScore> getScores() {
        return driverService.getDriverScores();
    }

    @GetMapping("/profile")
    public User getProfile() {
        return driverService.getDriver();
    }
}
