package com.example.telematics_fleet_management.repository;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverScoreRepository extends JpaRepository<DriverScore,Integer> {
    List<DriverScore> findByDriver(User driver);
    DriverScore findTopByDriverOrderByScoringDateDesc(User driver);
    DriverScore findTopByDriverOrderByScoreIdDesc(User driver);
}
