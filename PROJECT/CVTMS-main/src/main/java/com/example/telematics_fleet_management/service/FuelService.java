package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.FuelLog;
import com.example.telematics_fleet_management.repository.FuelLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FuelService {

    private final FuelLogRepository fuelLogRepository;

    public FuelService(FuelLogRepository fuelLogRepository) {
        this.fuelLogRepository = fuelLogRepository;
    }

    public List<FuelLog> getAllFuelLogs() {
        log.info("Fetching all fuel logs from the database");
        return fuelLogRepository.findAll();
    }

    public long getTotalFuelLogs() {
        log.info("Counting total fuel logs in the database");
        return fuelLogRepository.count();
    }

    public double getTotalFuelConsumed() {
        log.info("Calculating total fuel consumed from all fuel logs");
        return fuelLogRepository.findAll()
                .stream()
                .map(fuelLog -> fuelLog.getLitresRefilled())
                .reduce(0.0, (a, b) -> a + b);
    }

    public double getTotalFuelCost() {
        log.info("Calculating total fuel cost from all fuel logs");
        return fuelLogRepository.findAll()
                .stream()
                .map(fuelLog -> fuelLog.getCostAmount())
                .reduce(0.0, (a, b) -> a + b);
    }

    public double getAverageRefillAmount() {
        List<FuelLog> logs = fuelLogRepository.findAll();
        if (logs.isEmpty()) {
            return 0;
        }
        log.info("Calculating average refill amount from all fuel logs");
        return logs.stream()
                .map(fuelLog -> fuelLog.getLitresRefilled())
                .reduce(0.0, (a, b) -> a + b) / logs.size();
    }
}