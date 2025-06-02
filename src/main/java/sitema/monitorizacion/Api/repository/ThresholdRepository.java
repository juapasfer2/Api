package sitema.monitorizacion.Api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sitema.monitorizacion.Api.model.Threshold;
import sitema.monitorizacion.Api.model.VitalType;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    
    List<Threshold> findByType(VitalType type);
    
    Optional<Threshold> findByTypeId(Long typeId);
} 