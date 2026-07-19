package com.example.telematics_fleet_management.repository;

import com.example.telematics_fleet_management.model.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Integer> {
}
