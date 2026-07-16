package com.genc.patient.service;

import com.genc.patient.model.Patient;
import com.genc.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    private PatientRepository repository;

    @Autowired
    private DoctorClient doctorClient;

    public Patient add(Patient patient) {
        return repository.save(patient);
    }

    public List<Patient> getAll() {
        List<Patient> patients = repository.findAll();
        patients.stream().map(patient -> {
            patient.setDoctor(doctorClient.getDoctor(patient.getId()));
            return patient;
        }).collect(Collectors.toList());
        return patients;
    }

    public Patient getById(Long id) {
        Patient patient = repository.findById(id).orElseThrow(() -> new RuntimeException("Patient Not Found"));
        patient.setDoctor(doctorClient.getDoctor(patient.getDoctorId()));
        return patient;
    }

}
