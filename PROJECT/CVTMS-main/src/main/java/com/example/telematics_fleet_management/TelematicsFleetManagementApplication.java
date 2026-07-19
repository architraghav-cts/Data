package com.example.telematics_fleet_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelematicsFleetManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelematicsFleetManagementApplication.class, args);
    }
}