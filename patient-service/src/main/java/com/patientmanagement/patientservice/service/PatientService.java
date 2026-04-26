package com.patientmanagement.patientservice.service;

import com.patientmanagement.patientservice.dto.PatientRequestDTO;
import com.patientmanagement.patientservice.dto.PatientResponseDTO;
import com.patientmanagement.patientservice.model.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientResponseDTO> getPatients();

    PatientResponseDTO createPatient(PatientRequestDTO patientDTO);

    PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientDTO);

    void deletePatient(UUID id);
}
