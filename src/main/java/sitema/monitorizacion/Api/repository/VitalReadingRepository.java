package sitema.monitorizacion.Api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sitema.monitorizacion.Api.model.Patient;
import sitema.monitorizacion.Api.model.VitalReading;
import sitema.monitorizacion.Api.model.VitalType;

@Repository
public interface VitalReadingRepository extends JpaRepository<VitalReading, Long> {
    
    List<VitalReading> findByPatient(Patient patient);
    
    List<VitalReading> findByPatientAndType(Patient patient, VitalType type);
    
    List<VitalReading> findByPatientAndTimestampBetween(Patient patient, LocalDateTime start, LocalDateTime end);
    
    List<VitalReading> findByPatientAndTypeAndTimestampBetween(Patient patient, VitalType type, LocalDateTime start, LocalDateTime end);
} 