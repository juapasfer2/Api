package sitema.monitorizacion.Api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vital_types")
public class VitalType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    
    @NotBlank
    @Column(nullable = false)
    private String unit;
    
    @NotNull
    @Column(name = "normal_min", nullable = false)
    private Double normalMin;
    
    @NotNull
    @Column(name = "normal_max", nullable = false)
    private Double normalMax;
    
    @OneToMany(mappedBy = "type")
    private List<VitalReading> readings = new ArrayList<>();
    
    @OneToMany(mappedBy = "type")
    private List<Threshold> thresholds = new ArrayList<>();
} 