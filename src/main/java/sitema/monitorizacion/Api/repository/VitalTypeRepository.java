package sitema.monitorizacion.Api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sitema.monitorizacion.Api.model.VitalType;

@Repository
public interface VitalTypeRepository extends JpaRepository<VitalType, Long> {
    
    Optional<VitalType> findByName(String name);
} 