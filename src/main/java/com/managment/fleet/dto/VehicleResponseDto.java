package com.managment.fleet.dto;

import com.managment.fleet.domain.Vehicle;

import java.time.LocalDateTime;

public record VehicleResponseDto(
        String licensePlate,
        String model,
        String manufacturer,
        Integer year,
        String status,
        String typeofcar,
        LocalDateTime createdAt,
        Double mileage,
        Double nextMaintenanceMileage,
        Double rentalValue
) {
    public static VehicleResponseDto from(Vehicle vehicle) {
        return new VehicleResponseDto(
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

        );
    }
}
