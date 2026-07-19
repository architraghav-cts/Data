package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.repository.TripRepository;
import com.example.telematics_fleet_management.repository.UserRepository;
import com.example.telematics_fleet_management.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;


    public AdminService(UserRepository userRepository, VehicleRepository vehicleRepository, TripRepository tripRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users from the database");
        return userRepository.findAll();
    }

    public long getTotalUsers() {
        log.info("Counting total users in the database");
        return userRepository.count();
    }

    public long getTotalVehicles() {
        log.info("Counting total vehicles in the database");
        return vehicleRepository.count();
    }

    public long getTotalTrips() {
        log.info("Counting total trips in the database");
        return tripRepository.count();
    }
}
