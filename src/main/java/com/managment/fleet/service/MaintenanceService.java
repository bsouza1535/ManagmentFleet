package com.managment.fleet.service;

import com.managment.fleet.domain.Maintenance;
import com.managment.fleet.domain.MaintenanceStatus;
import com.managment.fleet.domain.PartsAutoMaintenance;
import com.managment.fleet.domain.Vehicle;
import com.managment.fleet.dto.MaintenanceRequestDto;
import com.managment.fleet.dto.MaintenanceResponseDto;
import com.managment.fleet.exception.BusinessException;
import com.managment.fleet.exception.NotFoundException;
import com.managment.fleet.repository.MaintenanceRepository;
import com.managment.fleet.repository.PartsAutoMaintenanceRepository;
import com.managment.fleet.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final PartsAutoMaintenanceRepository partsAutoMaintenanceRepository;

    @Transactional
    public MaintenanceResponseDto createMaintenance(MaintenanceRequestDto dto) {
        Vehicle vehicle = vehicleRepository.findByLicensePlateIgnoreCase(dto.licensePlate())
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + dto.licensePlate() + " not found"));

        // Automaticamente marca o veículo como em manutenção ao registrar
        if (vehicle.getStatus() != Vehicle.VehicleStatus.MAINTENANCE) {
            vehicle.setStatus(Vehicle.VehicleStatus.MAINTENANCE);
            vehicleRepository.save(vehicle);
        }

        Maintenance maintenance = Maintenance.builder()
                .vehicle(vehicle)
                .description(dto.description())
                .date(dto.date())
                .partsAuto(dto.partsAuto())
                .autorepair(dto.repairs())
                .priceOfMaintanence(dto.priceOfMaintanence())
                .next_maintenance_mileage(dto.next_maintenance_mileage())
                .KilometersOfMaintenance(dto.mileageOfMaintanence())
                .typeOfMaintenance(Maintenance.MaintenanceType.valueOf(dto.typeOfMaintenance()))
                .status(dto.status() != null ? dto.status() : MaintenanceStatus.SCHEDULED)
                .build();

        Maintenance saved = maintenanceRepository.save(maintenance);

        Vehicle vehiclenextmileage = saved.getVehicle();
        PartsAutoMaintenance partsauto = PartsAutoMaintenance.builder().build();

        vehiclenextmileage.setNextMaintenanceMileage(saved.getNext_maintenance_mileage());
        partsauto.setManutencao(maintenance);

        vehicleRepository.save(vehiclenextmileage);
        partsAutoMaintenanceRepository.save(partsauto);


        return MaintenanceResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<MaintenanceResponseDto> findAll(Pageable pageable) {
        return maintenanceRepository.findAll(pageable)
                .map(MaintenanceResponseDto::from);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceResponseDto> findByVehiclePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlateIgnoreCase(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + licensePlate + " not found"));

        return maintenanceRepository.findByVehicleIdOrderByDateDesc(vehicle.getId())
                .stream()
                .map(MaintenanceResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MaintenanceResponseDto findById(String id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance with ID " + id + " not found"));

        return MaintenanceResponseDto.from(maintenance);
    }

    @Transactional
    public MaintenanceResponseDto updateMaintenance(String id, MaintenanceRequestDto dto) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance with ID " + id + " not found"));

        // Verifica se está tentando mudar de veículo
        if (!maintenance.getVehicle().getLicensePlate().equalsIgnoreCase(dto.licensePlate())) {
            Vehicle newVehicle = vehicleRepository.findByLicensePlateIgnoreCase(dto.licensePlate())
                    .orElseThrow(() -> new NotFoundException("Vehicle with plate " + dto.licensePlate() + " not found"));
            maintenance.setVehicle(newVehicle);
        }

        maintenance.setDescription(dto.description());
        maintenance.setDate(dto.date());

        if (dto.status() != null) {
            maintenance.setStatus(dto.status());
        }

        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponseDto.from(updated);
    }

    @Transactional
    public MaintenanceResponseDto cancelMaintenance(String id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance with ID " + id + " not found"));

        // Soft delete - apenas marca como cancelada
        maintenance.setStatus(MaintenanceStatus.CANCELLED);

        Maintenance updated = maintenanceRepository.save(maintenance);

        // Retornar DTO mapeado
        return mapToResponseDto(updated);
    }

    private MaintenanceResponseDto mapToResponseDto(Maintenance maintenance) {
        return new MaintenanceResponseDto(
                maintenance.getId(),
                maintenance.getVehicle().getLicensePlate(),
                maintenance.getVehicle().getModel(),
                maintenance.getDescription(),
                maintenance.getDate(),
                maintenance.getPartsAuto(),
                maintenance.getTypeOfMaintenance().name(),
                maintenance.getAutorepair(),
                maintenance.getPriceOfMaintanence(),
                maintenance.getKilometersOfMaintenance(),
                maintenance.getNext_maintenance_mileage(),
                maintenance.getStatus()
        );
    }

    @Transactional
    public MaintenanceResponseDto startMaintenance(String id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance with ID " + id + " not found"));

        if (maintenance.getStatus() != MaintenanceStatus.SCHEDULED) {
            throw new BusinessException("Maintenance must be in SCHEDULED status to start");
        }

        maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);
        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponseDto.from(updated);
    }

    @Transactional
    public MaintenanceResponseDto completeMaintenance(String id, Double completionMileage) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance with ID " + id + " not found"));

        if (maintenance.getStatus() != MaintenanceStatus.IN_PROGRESS) {
            throw new BusinessException("Maintenance must be in IN_PROGRESS status to complete");
        }

        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        Maintenance updated = maintenanceRepository.save(maintenance);

        // Atualiza a quilometragem do veículo se fornecida
        if (completionMileage != null && completionMileage > 0) {
            Vehicle vehicle = maintenance.getVehicle();
            vehicle.setMileage(completionMileage);
            vehicleRepository.save(vehicle);
        }

        return MaintenanceResponseDto.from(updated);
    }

}

