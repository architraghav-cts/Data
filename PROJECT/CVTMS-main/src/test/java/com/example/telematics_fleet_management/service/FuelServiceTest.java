package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.FuelLog;
import com.example.telematics_fleet_management.repository.FuelLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuelServiceTest {

    @Mock
    private FuelLogRepository fuelLogRepository;

    @InjectMocks
    private FuelService fuelService;

    @Test
    void getTotalFuelLogsReturnsRepositoryCount() {
        when(fuelLogRepository.count()).thenReturn(7L);
        long result = fuelService.getTotalFuelLogs();
        assertEquals(7L, result);
    }

    @Test
    void getTotalFuelConsumedReturnsSumOfAllRefilledLitres() {
        FuelLog first = new FuelLog();
        first.setLitresRefilled(10.5);
        FuelLog second = new FuelLog();
        second.setLitresRefilled(20.0);
        FuelLog third = new FuelLog();
        third.setLitresRefilled(4.5);

        when(fuelLogRepository.findAll()).thenReturn(List.of(first, second, third));
        double result = fuelService.getTotalFuelConsumed();
        assertEquals(35.0, result);
    }
}