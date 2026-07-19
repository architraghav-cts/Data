package com.example.telematics_fleet_management.utils;

import com.example.telematics_fleet_management.model.Telemetry;
import com.example.telematics_fleet_management.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Slf4j
public class DummyTelemetryGenerator {

    private final Random random = new Random(); // It is used to generate random telemetry data

    public Telemetry generate(Vehicle vehicle) {

        Telemetry telemetry = new Telemetry();

        telemetry.setVehicle(vehicle);

        telemetry.setLatitude(9.9252 + random.nextDouble() * 0.02);

        telemetry.setLongitude(78.1198 + random.nextDouble() * 0.02);

        telemetry.setSpeed(40 + random.nextDouble() * 60);

        telemetry.setFuelLevel(20 + random.nextDouble() * 80);

        telemetry.setRecordedAt(
                LocalDateTime.now().minusMinutes(random.nextInt(30)));

        log.info("Generated dummy telemetry data for vehicle");
        return telemetry;
    }
}