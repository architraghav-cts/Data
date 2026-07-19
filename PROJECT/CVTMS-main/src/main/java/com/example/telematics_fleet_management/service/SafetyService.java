package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.repository.DriverScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SafetyService {

    private final DriverScoreRepository driverScoreRepository;

    public SafetyService(DriverScoreRepository driverScoreRepository) {
        this.driverScoreRepository = driverScoreRepository;
    }

    public List<DriverScore> getAllDriverScores() {
        log.info("Fetching all driver scores from the database");
        return driverScoreRepository.findAll();
    }

    public long getTotalSafetyReports() {
        log.info("Counting total safety reports in the database");
        return driverScoreRepository.count();
    }

    public double getAverageSafetyScore() {

        List<DriverScore> scores =
                driverScoreRepository.findAll();

        log.info("Calculating average safety score from all driver scores");
        if (scores.isEmpty()) {
            return 0;
        }

        return scores.stream()
                .mapToDouble(DriverScore::getSafetyScore)
                .average()
                .orElse(0);
    }

    public int getTotalOverspeedEvents() {
        log.info("Calculating total overspeed events from all driver scores");
        return driverScoreRepository.findAll()
                .stream()
                .mapToInt(DriverScore::getOverspeedCount)
                .sum();
    }

    public int getTotalHarshEvents() {
        log.info("Calculating total harsh events from all driver scores");
        return driverScoreRepository.findAll()
                .stream()
                .mapToInt(DriverScore::getHarshEventCount)
                .sum();
    }
}
