package com.patientmanagement.patientservice.mapper;

import com.patientmanagement.patientservice.dto.PatientRequestDTO;
import com.patientmanagement.patientservice.dto.PatientResponseDTO;
import com.patientmanagement.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public static Patient toModel(PatientRequestDTO patientDTO) {
        return Patient.builder()
                .name(patientDTO.getName())
                .email(patientDTO.getEmail())
                .address(patientDTO.getAddress())
                .dateOfBirth(LocalDate.parse(patientDTO.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientDTO.getRegisteredDate()))
                .build();
    }
}
