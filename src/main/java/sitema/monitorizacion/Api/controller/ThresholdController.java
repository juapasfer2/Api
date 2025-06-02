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
import sitema.monitorizacion.Api.dto.ThresholdRequest;
import sitema.monitorizacion.Api.dto.ThresholdResponse;
import sitema.monitorizacion.Api.service.ThresholdService;

@RestController
@RequestMapping("/api/admin/thresholds")
@RequiredArgsConstructor
public class ThresholdController {
    
    private final ThresholdService thresholdService;
    
    @GetMapping
    public ResponseEntity<List<ThresholdResponse>> getAllThresholds() {
        return ResponseEntity.ok(thresholdService.getAllThresholds());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ThresholdResponse> getThresholdById(@PathVariable Long id) {
        return ResponseEntity.ok(thresholdService.getThresholdById(id));
    }
    
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<ThresholdResponse>> getThresholdsByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(thresholdService.getThresholdsByType(typeId));
    }
    
    @GetMapping("/type/{typeId}/single")
    public ResponseEntity<ThresholdResponse> getThresholdByTypeId(@PathVariable Long typeId) {
        return ResponseEntity.ok(thresholdService.getThresholdByTypeId(typeId));
    }
    
    @PostMapping
    public ResponseEntity<ThresholdResponse> createThreshold(@Valid @RequestBody ThresholdRequest thresholdRequest) {
        return new ResponseEntity<>(thresholdService.createThreshold(thresholdRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ThresholdResponse> updateThreshold(@PathVariable Long id, @Valid @RequestBody ThresholdRequest thresholdRequest) {
        return ResponseEntity.ok(thresholdService.updateThreshold(id, thresholdRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThreshold(@PathVariable Long id) {
        thresholdService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
} 