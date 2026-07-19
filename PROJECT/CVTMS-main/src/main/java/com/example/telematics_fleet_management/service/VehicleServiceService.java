package com.example.telematics_fleet_management.service;

import com.example.telematics_fleet_management.model.ServiceRecord;
import com.example.telematics_fleet_management.repository.ServiceRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceService {

    private final ServiceRecordRepository serviceRecordRepository;

    public VehicleServiceService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public List<ServiceRecord> getAllServiceRecords() {
        return serviceRecordRepository.findAll();
    }

    public long getTotalServices() {
        return serviceRecordRepository.count();
    }

    public double getTotalMaintenanceCost() {

        return serviceRecordRepository.findAll()
                .stream()
                .mapToDouble(ServiceRecord::getServiceCost)
                .sum();
    }

    public List<ServiceRecord> getUpcomingServices() {

        return serviceRecordRepository.findAll()
                .stream()
                .filter(record ->
                        record.getNextServiceDueDate() != null)
                .toList();
    }
}