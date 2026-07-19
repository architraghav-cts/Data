package com.example.telematics_fleet_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecord {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int serviceRecordId;

    @ManyToOne

    @JoinColumn(name = "vehicle_id")

    private Vehicle vehicle;

    private LocalDate serviceDate;

    private String serviceType;

    private int odometerReading;

    private Double serviceCost;

    private LocalDate nextServiceDueDate;
}

