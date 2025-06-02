package sitema.monitorizacion.Api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.VitalTypeRequest;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.service.VitalTypeService;

@RestController
@RequestMapping("/api/admin/vital-types")
@RequiredArgsConstructor
public class VitalTypeController {
    
    private final VitalTypeService vitalTypeService;
    
    @GetMapping
    public ResponseEntity<List<VitalTypeResponse>> getAllVitalTypes() {
        return ResponseEntity.ok(vitalTypeService.getAllVitalTypes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VitalTypeResponse> getVitalTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(vitalTypeService.getVitalTypeById(id));
    }
    
    @PostMapping
    public ResponseEntity<VitalTypeResponse> createVitalType(@Valid @RequestBody VitalTypeRequest vitalTypeRequest) {
        return new ResponseEntity<>(vitalTypeService.createVitalType(vitalTypeRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VitalTypeResponse> updateVitalType(@PathVariable Long id, @Valid @RequestBody VitalTypeRequest vitalTypeRequest) {
        return ResponseEntity.ok(vitalTypeService.updateVitalType(id, vitalTypeRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitalType(@PathVariable Long id) {
        vitalTypeService.deleteVitalType(id);
        return ResponseEntity.noContent().build();
    }
} 