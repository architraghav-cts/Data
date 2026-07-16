package com.genc.doctor.controller;

import com.genc.doctor.model.Doctor;
import com.genc.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService service;

    @PostMapping("/add")
    public Doctor add(@RequestBody Doctor doctor) {
        return service.add(doctor);
    }

    @GetMapping("/all")
    public List<Doctor> getAll() {
        return service.getAll();
    }

    @GetMapping("/id/{id}")
    public Doctor getById(@PathVariable("id") Long id){
        return service.getById(id);
    }
}
