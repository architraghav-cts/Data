package com.example.telematics_fleet_management.controller;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.service.SafetyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/safety")
public class SafetyController {

    private final SafetyService safetyService;

    public SafetyController(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "totalSafetyReports",
                safetyService.getTotalSafetyReports());

        response.put(
                "averageSafetyScore",
                safetyService.getAverageSafetyScore());

        response.put(
                "totalOverspeedEvents",
                safetyService.getTotalOverspeedEvents());

        response.put(
                "totalHarshEvents",
                safetyService.getTotalHarshEvents());

        return response;
    }

    @GetMapping("/scores")
    public List<DriverScore> getAllScores() {
        return safetyService.getAllDriverScores();
    }
}