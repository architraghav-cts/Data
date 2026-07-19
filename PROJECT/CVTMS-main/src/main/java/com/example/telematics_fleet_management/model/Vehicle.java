package com.example.telematics_fleet_management.model;

import com.example.telematics_fleet_management.model.enums.VehicleStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicleId;

    private String vin;

    private int registrationNumber;

    private String vehicleType;

    private String deviceImei;

    private VehicleStatus vehicleStatus;

    public Vehicle(String vin, int registrationNumber, String vehicleType, String deviceImei, VehicleStatus vehicleStatus) {
        this.vin = vin;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.deviceImei = deviceImei;
        this.vehicleStatus = vehicleStatus;
    }
}