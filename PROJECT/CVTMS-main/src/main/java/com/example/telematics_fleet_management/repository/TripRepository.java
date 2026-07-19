package com.example.telematics_fleet_management.repository;

import com.example.telematics_fleet_management.model.Trip;
import com.example.telematics_fleet_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    long countByDriver(User driver);
    List<Trip> findByDriver(User driver);
}