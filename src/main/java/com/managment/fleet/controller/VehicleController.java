package com.managment.fleet.controller;

import com.managment.fleet.dto.VehicleRequestDto;
import com.managment.fleet.dto.VehicleResponseDto;
import com.managment.fleet.service.VehicleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    private final VehicleService vehicleService;

    public VehicleController(VehicleService service) {
        this.vehicleService = service;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> create(@Valid @RequestBody VehicleRequestDto dto) {
        logger.info("Creating vehicle with license plate: {}", dto.licensePlate());
        return ResponseEntity.ok(vehicleService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDto>> findAllFintered(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String typeofcar,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        logger.info("Fetching vehicles with filters - plate: {}, model: {}, status: {}, typeofcar: {}", licensePlate, model, status, typeofcar);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(vehicleService.findAllFiltered(licensePlate, model, status, typeofcar, pageable));
    }

    @PutMapping("/{licensePlate}")
    public ResponseEntity<VehicleResponseDto> updateVehicleByLicensePlate(
            @PathVariable String licensePlate,
            @Valid @RequestBody VehicleRequestDto dto) {
        logger.info("Updating vehicle with license plate {}", licensePlate);
        return ResponseEntity.ok(vehicleService.updateByLicensePlate(licensePlate, dto));
    }

    @PatchMapping("/inactivate/{licensePlate}")
    public ResponseEntity<VehicleResponseDto> inactivateVehicle(
            @PathVariable String licensePlate) {
        VehicleResponseDto response = vehicleService.inactivateVehicle(licensePlate);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/active/{licensePlate}")
    public ResponseEntity<VehicleResponseDto> activeVehicle(
            @PathVariable String licensePlate) {
        VehicleResponseDto response = vehicleService.activeVehicle(licensePlate);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/maintenance/{licensePlate}")
    public ResponseEntity<VehicleResponseDto> maintenanceVehicle(
            @PathVariable String licensePlate) {
        VehicleResponseDto response = vehicleService.maintenanceVehicle(licensePlate);
        return ResponseEntity.ok(response);
    }

}
