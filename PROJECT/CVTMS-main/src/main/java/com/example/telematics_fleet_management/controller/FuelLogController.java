package com.example.telematics_fleet_management.controller;

import com.example.telematics_fleet_management.model.FuelLog;
import com.example.telematics_fleet_management.service.FuelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fuel-logs")
public class FuelLogController {

    private final FuelService fuelService;

    public FuelLogController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping
    public List<FuelLog> getAllFuelLogs() {
        return fuelService.getAllFuelLogs();
    }

    @GetMapping("/stats")
    public Map<String, Object> getFuelStats() {

        Map<String, Object> response = new HashMap<>();

        response.put("totalFuelLogs", fuelService.getTotalFuelLogs());
        response.put("totalFuelConsumed", fuelService.getTotalFuelConsumed());
        response.put("totalFuelCost", fuelService.getTotalFuelCost());
        response.put("averageRefillAmount", fuelService.getAverageRefillAmount());

        return response;
    }
}