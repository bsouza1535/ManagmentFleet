package com.managment.fleet.service;

import com.managment.fleet.domain.Driver;
import com.managment.fleet.domain.Fueling;
import com.managment.fleet.domain.Vehicle;
import com.managment.fleet.dto.FuelingRequestDto;
import com.managment.fleet.dto.FuelingResponseDto;
import com.managment.fleet.exception.NotFoundException;
import com.managment.fleet.repository.DriverRepository;
import com.managment.fleet.repository.FuelingRepository;
import com.managment.fleet.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FuelingService {
    // Implementação do serviço de abastecimento

    private final FuelingRepository fuelingRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Transactional
    public FuelingResponseDto createFueling(FuelingRequestDto dto) {
        Vehicle vehicle = vehicleRepository.findByLicensePlateIgnoreCase(dto.licensePlate())
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + dto.licensePlate() + " not found"));

        Driver driver = null;
        if (dto.driverName() != null && !dto.driverName().trim().isEmpty()) {
            driver = driverRepository.findByNameIgnoreCase(dto.driverName())
                    .orElse(null);
        }

        Fueling fueling = new Fueling();
        fueling.setFuelingId(UUID.randomUUID().toString());
        fueling.setVehicle(vehicle);
        fueling.setDriver(driver);
        fueling.setFuelingDate(dto.date());
        fueling.setLiters(dto.liters());
        fueling.setPricePerLiter(dto.pricePerLiter());
        fueling.setTotalCost(dto.totalCost());
        fueling.setOdometer(dto.odometer());
        fueling.setFuelType(dto.fuelType());
        fueling.setFuelStation(dto.fuelStation());
        fueling.setDescription(dto.description());

        Fueling saved = fuelingRepository.save(fueling);

        return FuelingResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<FuelingResponseDto> findAll(Pageable pageable) {
        return fuelingRepository.findAll(pageable)
                .map(FuelingResponseDto::from);
    }

    @Transactional(readOnly = true)
    public FuelingResponseDto findById(String id) {
        Fueling fueling = fuelingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fueling with id " + id + " not found"));
        return FuelingResponseDto.from(fueling);
    }

    @Transactional
    public FuelingResponseDto update(String id, FuelingRequestDto dto) {
        Fueling fueling = fuelingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fueling with id " + id + " not found"));

        Vehicle vehicle = vehicleRepository.findByLicensePlateIgnoreCase(dto.licensePlate())
                .orElseThrow(() -> new NotFoundException("Vehicle with plate " + dto.licensePlate() + " not found"));

        Driver driver = null;
        if (dto.driverName() != null && !dto.driverName().trim().isEmpty()) {
            driver = driverRepository.findByNameIgnoreCase(dto.driverName())
                    .orElse(null);
        }

        fueling.setVehicle(vehicle);
        fueling.setDriver(driver);
        fueling.setFuelingDate(dto.date());
        fueling.setLiters(dto.liters());
        fueling.setPricePerLiter(dto.pricePerLiter());
        fueling.setTotalCost(dto.totalCost());
        fueling.setOdometer(dto.odometer());
        fueling.setFuelType(dto.fuelType());
        fueling.setFuelStation(dto.fuelStation());

        Fueling updated = fuelingRepository.save(fueling);

        return FuelingResponseDto.from(updated);
    }

    @Transactional
    public void delete(String id) {
        if (!fuelingRepository.existsById(id)) {
            throw new NotFoundException("Fueling with id " + id + " not found");
        }
        fuelingRepository.deleteById(id);
    }

}

