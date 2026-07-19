package com.example.telematics_fleet_management.utils;

import com.example.telematics_fleet_management.model.Trip;
import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.model.enums.TripStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Slf4j
public class DummyTripGenerator {

    private final Random random = new Random();

    private LocalDateTime lastTripEnd =
            LocalDateTime.now().minusDays(5);

    public Trip generate(Vehicle vehicle, User driver) {

        int gapHours = 1 + random.nextInt(3);

        LocalDateTime start =
                lastTripEnd.plusHours(gapHours);

        int durationHours =
                1 + random.nextInt(8);

        LocalDateTime end =
                start.plusHours(durationHours);

        Trip trip = new Trip();

        trip.setVehicle(vehicle);
        trip.setDriver(driver);

        trip.setStartTime(start);
        trip.setEndTime(end);

        trip.setDistanceKm(
                50 + random.nextDouble() * 450
        );

        trip.setTripStatus(
                TripStatus.COMPLETED
        );

        lastTripEnd = end;

        log.info("Generated dummy trip for vehicle and driver");
        return trip;
    }
}
