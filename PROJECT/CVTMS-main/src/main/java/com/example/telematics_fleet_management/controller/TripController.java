package com.example.telematics_fleet_management.controller;

import com.example.telematics_fleet_management.model.Trip;
import com.example.telematics_fleet_management.service.TripService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> response = new HashMap<>();

        response.put("totalTrips", tripService.getTotalTrips());
        response.put("trips", tripService.getAllTrips());

        return response;
    }
}
