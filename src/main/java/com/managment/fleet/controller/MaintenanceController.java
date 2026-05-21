package com.managment.fleet.controller;

import com.managment.fleet.dto.MaintenanceRequestDto;
import com.managment.fleet.dto.MaintenanceResponseDto;
import com.managment.fleet.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDto> createMaintenance(@Valid @RequestBody MaintenanceRequestDto dto) {
        logger.info("Creating maintenance for vehicle: {}", dto.licensePlate());
        MaintenanceResponseDto response = maintenanceService.createMaintenance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<MaintenanceResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching all maintenances - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(maintenanceService.findAll(pageable));
    }

    @GetMapping("/vehicle/{licensePlate}")
    public ResponseEntity<List<MaintenanceResponseDto>> findByVehicle(@PathVariable String licensePlate) {
        logger.info("Fetching maintenances for vehicle: {}", licensePlate);
        return ResponseEntity.ok(maintenanceService.findByVehiclePlate(licensePlate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> findById(@PathVariable String id) {
        logger.info("Fetching maintenance by ID: {}", id);
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> updateMaintenance(
            @PathVariable String id,
            @Valid @RequestBody MaintenanceRequestDto dto) {
        logger.info("Updating maintenance ID: {}", id);
        return ResponseEntity.ok(maintenanceService.updateMaintenance(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<MaintenanceResponseDto> cancelMaintenance(@PathVariable String id) {
        logger.info("Canceling maintenance ID: {}", id);
        MaintenanceResponseDto response = maintenanceService.cancelMaintenance(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/start")
    public ResponseEntity<MaintenanceResponseDto> startMaintenance(@PathVariable String id) {
        logger.info("Starting maintenance ID: {}", id);
        MaintenanceResponseDto response = maintenanceService.startMaintenance(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<MaintenanceResponseDto> completeMaintenance(
            @PathVariable String id,
            @RequestParam(required = false) Double completionMileage) {
        logger.info("Completing maintenance ID: {} with mileage: {}", id, completionMileage);
        MaintenanceResponseDto response = maintenanceService.completeMaintenance(id, completionMileage);
        return ResponseEntity.ok(response);
    }
}
