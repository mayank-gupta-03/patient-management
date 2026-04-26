package com.patientmanagement.patientservice.service;

import billing.BillingRequest;
import com.patientmanagement.patientservice.dto.PatientRequestDTO;
import com.patientmanagement.patientservice.dto.PatientResponseDTO;
import com.patientmanagement.patientservice.exception.EmailAlreadyExistsException;
import com.patientmanagement.patientservice.exception.PatientNotFoundException;
import com.patientmanagement.patientservice.grpc.BillingServiceGrpcClient;
import com.patientmanagement.patientservice.mapper.PatientMapper;
import com.patientmanagement.patientservice.model.Patient;
import com.patientmanagement.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    @Override
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients
                .stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO) {
        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientDTO));

        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .build();
        billingServiceGrpcClient.createBillingAccount(billingRequest);

        return PatientMapper.toDTO(patient);
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientDTO.getEmail());
        }

        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setEmail(patientDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientDTO.getDateOfBirth()));

        return PatientMapper.toDTO(patientRepository.save(patient));
    }

    @Override
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
