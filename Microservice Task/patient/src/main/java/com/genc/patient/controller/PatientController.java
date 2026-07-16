package com.genc.patient.controller;

import com.genc.patient.model.Patient;
import com.genc.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService service;

    @PostMapping("/add")
    public Patient add(@RequestBody Patient patient) {
        return service.add(patient);
    }

    @GetMapping("/all")
    public List<Patient> getAll() {
        return service.getAll();
    }

    @GetMapping("/id/{id}")
    public Patient getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }
}
