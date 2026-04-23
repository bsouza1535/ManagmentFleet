package com.managment.fleet.controller;

import com.managment.fleet.dto.java.DriverRequestDto;
import com.managment.fleet.dto.java.DriverResponseDto;
import com.managment.fleet.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

    private final DriverService driverService;


    @PostMapping
    public ResponseEntity<DriverResponseDto> createDriver(@Valid @RequestBody DriverRequestDto dto) {
        logger.info("Creating new driver with CNH {}", dto.cnh());
        DriverResponseDto created = driverService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Page<DriverResponseDto>> listDrivers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cnh,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        logger.info("Listing drivers with filters name={}, cnh={}, status={}", name, cnh, status);
        Page<DriverResponseDto> result = driverService.findAllFiltered(name, cnh, status, pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<DriverResponseDto> updateDriver(@Valid @RequestBody DriverRequestDto dto) {
        logger.info("Updating driver with ID {}", dto.id());
        DriverResponseDto updated = driverService.update(dto);
        return ResponseEntity.ok(updated);
    }
}