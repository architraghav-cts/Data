package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.Telemetry;
import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.model.enums.Role;
import com.example.telematics_fleet_management.model.enums.VehicleStatus;
import com.example.telematics_fleet_management.repository.TelemetryRepository;
import com.example.telematics_fleet_management.repository.UserRepository;
import com.example.telematics_fleet_management.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final TelemetryRepository telemetryRepository;
    private final UserRepository userRepository;

    public VehicleService(
            VehicleRepository vehicleRepository,
            TelemetryRepository telemetryRepository,
            UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.telemetryRepository = telemetryRepository;
        this.userRepository = userRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle registerVehicle(
            String vin,
            int registrationNumber,
            String vehicleType,
            String deviceImei,
            String fleetManagerEmail) {

        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("VIN is required");
        }
        if (vehicleType == null || vehicleType.isBlank()) {
            throw new IllegalArgumentException("Vehicle type is required");
        }
        if (deviceImei == null || deviceImei.isBlank()) {
            throw new IllegalArgumentException("Device IMEI is required");
        }

        User fleetManager = userRepository.findByEmail(fleetManagerEmail);
        if (fleetManager == null || fleetManager.getRole() != Role.FLEET_MANAGER) {
            throw new IllegalArgumentException("Only Fleet Manager can register vehicles");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setVehicleType(vehicleType);
        vehicle.setDeviceImei(deviceImei);
        vehicle.setVehicleStatus(VehicleStatus.ACTIVE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Telemetry telemetry = new Telemetry();
        telemetry.setVehicle(savedVehicle);
        telemetry.setLatitude(0.0);
        telemetry.setLongitude(0.0);
        telemetry.setSpeed(0.0);
        telemetry.setFuelLevel(100.0);
        telemetry.setRecordedAt(LocalDateTime.now());
        telemetryRepository.save(telemetry);
        log.info("Registered new vehicle with VIN");
        return savedVehicle;
    }
}
