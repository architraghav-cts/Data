package com.example.telematics_fleet_management.config;

import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.model.Vehicle;
import com.example.telematics_fleet_management.repository.UserRepository;
import com.example.telematics_fleet_management.repository.VehicleRepository;
import com.example.telematics_fleet_management.utils.DummyUserGenerator;
import com.example.telematics_fleet_management.utils.DummyVehicleGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class DataLoader implements CommandLineRunner {

    private UserRepository userRepository;
    private DummyUserGenerator userGenerator;
    private VehicleRepository vehicleRepository;
    private DummyVehicleGenerator vehicleGenerator;

    public DataLoader(UserRepository userRepository, DummyUserGenerator userGenerator, VehicleRepository vehicleRepository, DummyVehicleGenerator vehicleGenerator) {
        this.userRepository = userRepository;
        this.userGenerator = userGenerator;
        this.vehicleRepository = vehicleRepository;
        this.vehicleGenerator = vehicleGenerator;
    }

    @Override
    public void run(String... args) {
        loadUsers();
        loadVehicles();
        log.info("Dummy data loaded successfully.");
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            List<User> users = userGenerator.generateUsers();
            userRepository.saveAll(users);
            log.info("Dummy users inserted.");
        }
    }

    private void loadVehicles() {
        if (vehicleRepository.count() == 0) {
            List<Vehicle> vehicles = vehicleGenerator.generateVehicles();
            vehicleRepository.saveAll(vehicles);
            log.info("Dummy vehicles inserted.");
        }
    }
}