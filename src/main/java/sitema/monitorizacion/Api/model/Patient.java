package sitema.monitorizacion.Api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "patients")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @NotBlank
    @Column(name = "medical_record_number", nullable = false, unique = true)
    private String medicalRecordNumber;
    
    @ManyToMany(mappedBy = "patients")
    private List<User> nurses = new ArrayList<>();
    
    @OneToMany(mappedBy = "patient")
    private List<VitalReading> vitalReadings = new ArrayList<>();
} 