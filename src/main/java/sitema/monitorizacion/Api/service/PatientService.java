package sitema.monitorizacion.Api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.PatientRequest;
import sitema.monitorizacion.Api.dto.PatientResponse;
import sitema.monitorizacion.Api.dto.UserResponse;
import sitema.monitorizacion.Api.model.Patient;
import sitema.monitorizacion.Api.model.User;
import sitema.monitorizacion.Api.repository.PatientRepository;
import sitema.monitorizacion.Api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PatientService {
    
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }
    
    public PatientResponse getPatientById(Long id) {
        Patient patient = getPatientEntityById(id);
        return mapToPatientResponse(patient);
    }
    
    public List<PatientResponse> getPatientsByNurseId(Long nurseId) {
        User nurse = userRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Enfermera no encontrada con ID: " + nurseId));
        
        return patientRepository.findByNursesContaining(nurse).stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        Patient patient = Patient.builder()
                .firstName(patientRequest.getFirstName())
                .lastName(patientRequest.getLastName())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .medicalRecordNumber(patientRequest.getMedicalRecordNumber())
                .nurses(new ArrayList<>())
                .build();
        
        if (patientRequest.getNurseIds() != null && !patientRequest.getNurseIds().isEmpty()) {
            List<User> nurses = userRepository.findAllById(patientRequest.getNurseIds());
            for (User nurse : nurses) {
                if (nurse.getPatients() == null) {
                    nurse.setPatients(new ArrayList<>());
                }
                nurse.getPatients().add(patient);
            }
        }
        
        Patient savedPatient = patientRepository.save(patient);
        return mapToPatientResponse(savedPatient);
    }
    
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest patientRequest) {
        Patient patient = getPatientEntityById(id);
        
        patient.setFirstName(patientRequest.getFirstName());
        patient.setLastName(patientRequest.getLastName());
        patient.setDateOfBirth(patientRequest.getDateOfBirth());
        patient.setMedicalRecordNumber(patientRequest.getMedicalRecordNumber());
        
        // Actualizar enfermeras asignadas
        if (patientRequest.getNurseIds() != null) {
            // Primero eliminar al paciente de todas las enfermeras actuales
            for (User nurse : patient.getNurses()) {
                nurse.getPatients().remove(patient);
            }
            
            // Luego asignar al paciente a las nuevas enfermeras
            List<User> nurses = userRepository.findAllById(patientRequest.getNurseIds());
            for (User nurse : nurses) {
                if (nurse.getPatients() == null) {
                    nurse.setPatients(new ArrayList<>());
                }
                nurse.getPatients().add(patient);
            }
        }
        
        Patient updatedPatient = patientRepository.save(patient);
        return mapToPatientResponse(updatedPatient);
    }
    
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = getPatientEntityById(id);
        
        // Eliminar el paciente de la lista de pacientes de cada enfermera
        for (User nurse : patient.getNurses()) {
            nurse.getPatients().remove(patient);
        }
        
        patientRepository.delete(patient);
    }
    
    @Transactional
    public PatientResponse assignNurseToPatient(Long patientId, Long nurseId) {
        Patient patient = getPatientEntityById(patientId);
        User nurse = userRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Enfermera no encontrada con ID: " + nurseId));
        
        if (nurse.getPatients() == null) {
            nurse.setPatients(new ArrayList<>());
        }
        
        if (!nurse.getPatients().contains(patient)) {
            nurse.getPatients().add(patient);
            userRepository.save(nurse);
        }
        
        return mapToPatientResponse(patient);
    }
    
    @Transactional
    public PatientResponse removeNurseFromPatient(Long patientId, Long nurseId) {
        Patient patient = getPatientEntityById(patientId);
        User nurse = userRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Enfermera no encontrada con ID: " + nurseId));
        
        if (nurse.getPatients() != null && nurse.getPatients().contains(patient)) {
            nurse.getPatients().remove(patient);
            userRepository.save(nurse);
        }
        
        return mapToPatientResponse(patient);
    }
    
    // Método auxiliar para obtener una entidad Patient por ID
    private Patient getPatientEntityById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    }
    
    // Mapear Patient a PatientResponse
    private PatientResponse mapToPatientResponse(Patient patient) {
        List<UserResponse> nurseResponses = new ArrayList<>();
        
        if (patient.getNurses() != null) {
            nurseResponses = patient.getNurses().stream()
                    .map(nurse -> UserResponse.builder()
                            .id(nurse.getId())
                            .name(nurse.getName())
                            .email(nurse.getEmail())
                            .roleName(nurse.getRole() != null ? nurse.getRole().getName() : null)
                            .build())
                    .collect(Collectors.toList());
        }
        
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .medicalRecordNumber(patient.getMedicalRecordNumber())
                .nurses(nurseResponses)
                .build();
    }
} 