package com.managment.fleet.service;

import com.managment.fleet.domain.Vehicle;
import com.managment.fleet.dto.VehicleRequestDto;
import com.managment.fleet.dto.VehicleResponseDto;
import com.managment.fleet.exception.BusinessException;
import com.managment.fleet.exception.InvalidOperationException;
import com.managment.fleet.exception.NotFoundException;
import com.managment.fleet.repository.FuelingRepository;
import com.managment.fleet.repository.MaintenanceRepository;
import com.managment.fleet.repository.VehicleRepository;
import com.managment.fleet.repository.VehicleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository repository;
    private final MaintenanceRepository maintenanceRepository;
    private final FuelingRepository fuelingRepository;

    public VehicleService(VehicleRepository repository, MaintenanceRepository maintenanceRepository, FuelingRepository fuelingRepository) {
        this.repository = repository;
        this.maintenanceRepository = maintenanceRepository;
        this.fuelingRepository = fuelingRepository;
    }

    @Transactional
    public VehicleResponseDto create(VehicleRequestDto dto) {
        //String normalizedPlate = dto.licensePlate().toUpperCase();
        if (repository.existsByLicensePlate(dto.licensePlate())) {
            throw new BusinessException("Vehicle with license plate '" + dto.licensePlate() + "' already exists.");
        } else if (dto == null || dto.licensePlate() == null) {
            throw new BusinessException("Invalid vehicle data");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(dto.licensePlate());
        vehicle.setModel(dto.model());
        vehicle.setManufacturer(dto.manufacturer());
        vehicle.setYear(dto.year());
        vehicle.setStatus(Vehicle.VehicleStatus.valueOf(dto.status()));
        vehicle.setTypeofcar(Vehicle.VehicleTypeOfCar.valueOf(dto.typeofcar()));
        vehicle.setMileage(dto.mileage());
        vehicle.setNextMaintenanceMileage(dto.nextMaintenanceMileage());
        vehicle.setRentalValue(dto.rentalValue());


        Vehicle saved = repository.save(vehicle);

        return new VehicleResponseDto(
                saved.getLicensePlate(),
                saved.getModel(),
                saved.getManufacturer(),
                saved.getYear(),
                saved.getStatus().name(),
                saved.getTypeofcar().name(),
                saved.getCreatedAt(),
                saved.getMileage(),
                saved.getNextMaintenanceMileage(),
                saved.getRentalValue()
        );
    }

    @Transactional
    public VehicleResponseDto inactivateVehicle(String licensePlate) {
        Vehicle vehicle = repository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + licensePlate + " not found."));

        vehicle.setStatus(Vehicle.VehicleStatus.INACTIVE);

        Vehicle updated = repository.save(vehicle);

        return new VehicleResponseDto(
                updated.getLicensePlate(),
                updated.getModel(),
                updated.getManufacturer(),
                updated.getYear(),
                updated.getStatus().name(),
                updated.getTypeofcar().name(),
                updated.getCreatedAt(),
                updated.getMileage(),
                updated.getNextMaintenanceMileage(),
                updated.getRentalValue()
        );
    }

    @Transactional
    public VehicleResponseDto activeVehicle(String licensePlate) {
        Vehicle vehicle = repository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + licensePlate + " not found."));

        vehicle.setStatus(Vehicle.VehicleStatus.ACTIVE);

        Vehicle updated = repository.save(vehicle);

        return new VehicleResponseDto(
                updated.getLicensePlate(),
                updated.getModel(),
                updated.getManufacturer(),
                updated.getYear(),
                updated.getStatus().name(),
                updated.getTypeofcar().name(),
                updated.getCreatedAt(),
                updated.getMileage(),
                updated.getNextMaintenanceMileage(),
                updated.getRentalValue()
        );
    }

    @Transactional
    public VehicleResponseDto maintenanceVehicle(String licensePlate) {
        Vehicle vehicle = repository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + licensePlate + " not found."));

        vehicle.setStatus(Vehicle.VehicleStatus.MAINTENANCE);

        Vehicle updated = repository.save(vehicle);

        return new VehicleResponseDto(
                updated.getLicensePlate(),
                updated.getModel(),
                updated.getManufacturer(),
                updated.getYear(),
                updated.getStatus().name(),
                updated.getTypeofcar().name(),
                updated.getCreatedAt(),
                updated.getMileage(),
                updated.getNextMaintenanceMileage(),
                updated.getRentalValue()
        );
    }

    @Transactional
    public VehicleResponseDto updateStatusVehicleByLicensePlate(String licensePlate, VehicleRequestDto dto) {
        Vehicle vehicle = repository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " +  licensePlate + " not found."));


        if (dto.status() != null) {
            vehicle.setStatus(Vehicle.VehicleStatus.valueOf(dto.status().toUpperCase()));
        }

        Vehicle updated = repository.save(vehicle);

        return new VehicleResponseDto(
                updated.getLicensePlate(),
                updated.getModel(),
                updated.getManufacturer(),
                updated.getYear(),
                updated.getStatus().name(),
                updated.getTypeofcar().name(),
                updated.getCreatedAt(),
                updated.getMileage(),
                updated.getNextMaintenanceMileage(),
                updated.getRentalValue()
        );

    }

    @Transactional
    public VehicleResponseDto updateByLicensePlate(String licensePlate, VehicleRequestDto dto) {
        Vehicle vehicle = repository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + licensePlate + " not found"));

        if (!vehicle.getLicensePlate().equalsIgnoreCase(dto.licensePlate())) {
            boolean hasHistory = maintenanceRepository.existsByVehicleId(vehicle.getId())
                    || fuelingRepository.existsByVehicleId((vehicle.getId()));

            if (hasHistory) {
                throw new InvalidOperationException(
                        "License plate cannot be changed because vehicle has maintenance or fueling history"
                );
            }

            if (repository.existsByLicensePlate(dto.licensePlate().toUpperCase())) {
                throw new BusinessException("Another vehicle with plate '" + dto.licensePlate() + "' already exists.");
            }

            vehicle.setLicensePlate(dto.licensePlate().toUpperCase());
        }

        vehicle.setModel(dto.model());
        vehicle.setManufacturer(dto.manufacturer());
        vehicle.setYear(dto.year());
        vehicle.setMileage(dto.mileage());
        vehicle.setNextMaintenanceMileage(dto.nextMaintenanceMileage());
        vehicle.setRentalValue(dto.rentalValue());
        vehicle.setTypeofcar(Vehicle.VehicleTypeOfCar.valueOf(dto.typeofcar().toUpperCase()));

        try {
            vehicle.setStatus(Vehicle.VehicleStatus.valueOf(dto.status().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid status. Allowed values: ACTIVE or INACTIVE");
        }

        Vehicle updated = repository.save(vehicle);

        return new VehicleResponseDto(
                updated.getLicensePlate(),
                updated.getModel(),
                updated.getManufacturer(),
                updated.getYear(),
                updated.getStatus().name(),
                updated.getTypeofcar().name(),
                updated.getCreatedAt(),
                updated.getMileage(),
                updated.getNextMaintenanceMileage(),
                updated.getRentalValue()
        );
    }


    public Page<VehicleResponseDto> findAllFiltered(String licensePlate, String model, String status, String typeofcar, Pageable pageable) {
        Specification<Vehicle> spec = Specification
                .where(VehicleSpecification.licensePlateContains(licensePlate))
                .and(VehicleSpecification.modelContains(model))
                .and(VehicleSpecification.hasStatus(status))
                .and(VehicleSpecification.hasTypeOfCar(typeofcar));

        return repository.findAll(spec, pageable)
                .map(v -> new VehicleResponseDto(
                        v.getLicensePlate(),
                        v.getModel(),
                        v.getManufacturer(),
                        v.getYear(),
                        v.getStatus().name(),
                        v.getTypeofcar().name(),
                        v.getCreatedAt(),
                        v.getMileage(),
                        v.getNextMaintenanceMileage(),
                        v.getRentalValue()
                ));
    }

    public List<VehicleResponseDto> findByLicensePlate(String licensePlate) {
        Vehicle vehicle = repository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with ID " + licensePlate + " not found"));

        return Collections.singletonList(new VehicleResponseDto(
                vehicle.getLicensePlate(),
                vehicle.getModel(),
                vehicle.getManufacturer(),
                vehicle.getYear(),
                vehicle.getStatus().name(),
                vehicle.getTypeofcar().name(),
                vehicle.getCreatedAt(),
                vehicle.getMileage(),
                vehicle.getNextMaintenanceMileage(),
                vehicle.getRentalValue()
        ));
    }
}
