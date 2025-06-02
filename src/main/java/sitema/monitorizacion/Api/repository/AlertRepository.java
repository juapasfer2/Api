package sitema.monitorizacion.Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sitema.monitorizacion.Api.model.Alert;
import sitema.monitorizacion.Api.model.User;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    List<Alert> findByAcknowledged(boolean acknowledged);
    
    List<Alert> findByAcknowledgedBy(User user);
    
    @Query("SELECT a FROM Alert a WHERE a.reading.patient.id IN (SELECT p.id FROM Patient p JOIN p.nurses n WHERE n.id = :nurseId)")
    List<Alert> findAlertsByNurseId(Long nurseId);
    
    @Query("SELECT a FROM Alert a WHERE a.reading.patient.id IN (SELECT p.id FROM Patient p JOIN p.nurses n WHERE n.id = :nurseId) AND a.acknowledged = :acknowledged")
    List<Alert> findAlertsByNurseIdAndAcknowledged(Long nurseId, boolean acknowledged);
} 