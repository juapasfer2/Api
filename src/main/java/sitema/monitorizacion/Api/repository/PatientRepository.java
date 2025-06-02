package sitema.monitorizacion.Api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sitema.monitorizacion.Api.model.Patient;
import sitema.monitorizacion.Api.model.User;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);
    
    List<Patient> findByNursesContaining(User nurse);
} 