package com.genc.patient.service;

import com.genc.patient.model.Doctor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "doctor", url = "${doctor.service.url:http://localhost:8081}")
@FeignClient(name = "DOCTOR")
public interface DoctorClient {
    @GetMapping("/doctor/id/{id}")
    Doctor getDoctor(@PathVariable("id") Long id);
}