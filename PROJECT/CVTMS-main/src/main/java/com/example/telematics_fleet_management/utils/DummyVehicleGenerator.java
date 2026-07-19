package com.example.telematics_fleet_management.utils;

import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.model.enums.VehicleStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DummyVehicleGenerator {

    public List<Vehicle> generateVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.add(new Vehicle(
                "VIN1234567890",
                12345,
                "Sedan",
                "ABC123",
                VehicleStatus.ACTIVE
        ));

        log.info("Generated dummy vehicles");
        return vehicles;
    }
}