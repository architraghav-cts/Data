package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.repository.*;
import com.example.telematics_fleet_management.utils.DummyServiceRecordGenerator;
import com.example.telematics_fleet_management.utils.DummyTelemetryGenerator;
import com.example.telematics_fleet_management.utils.DummyTripGenerator;
import com.example.telematics_fleet_management.utils.DummyFuelLogGenerator;
import com.example.telematics_fleet_management.utils.DummyDriverScoreGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetrySimulationService {

    private final VehicleRepository vehicleRepository;
    private final TelemetryRepository telemetryRepository;
    private final TripRepository tripRepository;
    private final FuelLogRepository fuelLogRepository;
    private final ServiceRecordRepository serviceRecordRepository;
    private final UserRepository userRepository;
    private final DriverScoreRepository driverScoreRepository;
    private final DummyDriverScoreGenerator driverScoreGenerator;
    private final DummyTelemetryGenerator telemetryGenerator;
    private final DummyTripGenerator tripGenerator;
    private final DummyFuelLogGenerator fuelLogGenerator;
    private final DummyServiceRecordGenerator serviceGenerator;

    public TelemetrySimulationService(
            VehicleRepository vehicleRepository,
            TelemetryRepository telemetryRepository,
            TripRepository tripRepository,
            FuelLogRepository fuelLogRepository,
            ServiceRecordRepository serviceRecordRepository,
            UserRepository userRepository,
            DriverScoreRepository driverScoreRepository,
            DummyDriverScoreGenerator driverScoreGenerator,
            DummyTelemetryGenerator telemetryGenerator,
            DummyTripGenerator tripGenerator,
            DummyFuelLogGenerator fuelLogGenerator,
            DummyServiceRecordGenerator serviceGenerator) {

        this.vehicleRepository = vehicleRepository;
        this.telemetryRepository = telemetryRepository;
        this.tripRepository = tripRepository;
        this.fuelLogRepository = fuelLogRepository;
        this.serviceRecordRepository = serviceRecordRepository;
        this.userRepository = userRepository;
        this.driverScoreRepository = driverScoreRepository;
        this.driverScoreGenerator = driverScoreGenerator;
        this.telemetryGenerator = telemetryGenerator;
        this.tripGenerator = tripGenerator;
        this.fuelLogGenerator = fuelLogGenerator;
        this.serviceGenerator = serviceGenerator;
    }

    private static final Logger logger = LoggerFactory.getLogger(TelemetrySimulationService.class);

    @Scheduled(fixedRate = 5000)
    public void generateData() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        User driver = userRepository
                .findByEmail("driver@gmail.com");

        for (Vehicle vehicle : vehicles) {

            // Telemetry every 5 seconds
            telemetryRepository.save(
                    telemetryGenerator.generate(vehicle)
            );

            double x = Math.random();

            // 20% chance to create Trip
            if (x < 0.2 && driver != null) {
                tripRepository.save(
                        tripGenerator.generate(vehicle, driver)
                );
            }

            // 10% chance to create Fuel Log
            if (x < 0.1) {
                fuelLogRepository.save(
                        fuelLogGenerator.generate(vehicle)
                );
            }

            // 5% chance to create Service Record
            if (x < 0.05) {
                serviceRecordRepository.save(
                        serviceGenerator.generate(vehicle)
                );
            }

            if (x < 0.2 && driver != null) {
                DriverScore previousScore =
                        driverScoreRepository.findTopByDriverOrderByScoreIdDesc(driver);

                driverScoreRepository.save(
                        driverScoreGenerator.generate(
                                driver,
                                vehicle,
                                previousScore
                        )
                );
            }
        }

        logger.info("Telemetry data generated for all vehicles.");
    }
}