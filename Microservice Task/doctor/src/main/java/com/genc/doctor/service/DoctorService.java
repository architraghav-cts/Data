package com.genc.doctor.service;

import com.genc.doctor.model.Doctor;
import com.genc.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository repository;

    public Doctor add(Doctor doctor) {
        return repository.save(doctor);
    }

    public List<Doctor> getAll() {
        return repository.findAll();
    }

    public Doctor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
    }
}