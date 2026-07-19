package com.example.telematics_fleet_management.controller;

import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fleet/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> registerVehicle(
            @RequestParam String vin,
            @RequestParam int registrationNumber,
            @RequestParam String vehicleType,
            @RequestParam String deviceImei,
            @RequestParam String fleetManagerEmail) {

        Map<String, Object> response = new HashMap<>();

        try {
            Vehicle savedVehicle = vehicleService.registerVehicle(
                    vin,
                    registrationNumber,
                    vehicleType,
                    deviceImei,
                    fleetManagerEmail
            );

            response.put("success", true);
            response.put("message", "Vehicle registered successfully");
            response.put("vehicle", savedVehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}