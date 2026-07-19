package com.example.telematics_fleet_management.model;

import com.example.telematics_fleet_management.model.enums.TripStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tripId;
    @ManyToOne
    @JoinColumn(name = "vehicle_id") // Assuming Vehicle is the vehicle entity. one vehicle can have multiple trips
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "driver_id") // Assuming User is the driver entity. one driver can have multiple trips
    private User driver;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double distanceKm;
    private TripStatus tripStatus;
}

