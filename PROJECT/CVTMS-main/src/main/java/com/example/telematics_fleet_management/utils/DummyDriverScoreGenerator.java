package com.example.telematics_fleet_management.utils;

import com.example.telematics_fleet_management.model.DriverScore;
import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@Slf4j
public class DummyDriverScoreGenerator {

    private static final double BASE_SAFETY_SCORE = 100.0;
    private static final int HARSH_EVENT_PENALTY = 8;
    private static final int OVERSPEED_EVENT_PENALTY = 5;

    private final Random random = new Random();

    public DriverScore generate(User driver, Vehicle vehicle, DriverScore previousScore) {

        DriverScore score = new DriverScore();

        score.setDriver(driver);
        score.setVehicle(vehicle);

        score.setScoringDate(
                LocalDate.now()
                        .minusDays(random.nextInt(30))
        );

        int harshEventCount;
        int overspeedCount;

        if (previousScore == null) {
            harshEventCount = 0;
            overspeedCount = 0;
        } else {
            harshEventCount = previousScore.getHarshEventCount() + (1 + random.nextInt(2));
            overspeedCount = previousScore.getOverspeedCount() + random.nextInt(2);
        }

        score.setHarshEventCount(
                harshEventCount
        );

        score.setOverspeedCount(
                overspeedCount
        );

        double safetyScore = BASE_SAFETY_SCORE;

        safetyScore -= harshEventCount * HARSH_EVENT_PENALTY;
        safetyScore -= overspeedCount * OVERSPEED_EVENT_PENALTY;

        if (safetyScore < 0) {
            safetyScore = 0;
        }

        score.setSafetyScore(
                Math.round(safetyScore * 100.0) / 100.0
        );

        log.info("Generated dummy driver score for driver with safety score {}", score.getSafetyScore());
        return score;
    }
}