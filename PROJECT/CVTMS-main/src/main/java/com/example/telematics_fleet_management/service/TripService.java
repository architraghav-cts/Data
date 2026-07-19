package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.Trip;
import com.example.telematics_fleet_management.repository.TripRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> getAllTrips() {
        log.info("Fetching all trips from the database");
        return tripRepository.findAll();
    }

    public long getTotalTrips() {
        log.info("Counting total trips in the database");
        return tripRepository.count();
    }
}
