package com.example.telematics_fleet_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int telemetryId;

    @ManyToOne
    @JoinColumn(name = "vehicle_id") // It is used to establish a many-to-one relationship between Telemetry and Vehicle. Each telemetry record is associated with one vehicle.
    private Vehicle vehicle;         // Each vehicle can have multiple telemetry records.

    private Double latitude;

    private Double longitude;

    private Double speed;

    private Double fuelLevel;

    private LocalDateTime recordedAt;
}