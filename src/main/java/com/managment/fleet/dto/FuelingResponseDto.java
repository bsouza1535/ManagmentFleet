package com.managment.fleet.dto;

import com.managment.fleet.domain.Fueling;

import java.time.LocalDate;

public record FuelingResponseDto(
        String id,
        LocalDate date,
        String vehiclePlate,
        String vehicleModel,
        String driverName,
        Double liters,
        Double pricePerLiter,
        Double totalCost,
        Integer odometer,
        String fuelType,
        String fuelStation,
        String description
) {
    public static FuelingResponseDto from(Fueling fueling) {
        return new FuelingResponseDto(
                fueling.getFuelingId(),
                fueling.getFuelingDate(),
                fueling.getVehicle() != null ? fueling.getVehicle().getLicensePlate() : null,
                fueling.getVehicle() != null ?
                        fueling.getVehicle().getManufacturer() + " " + fueling.getVehicle().getModel() : null,
                fueling.getDriver() != null ? fueling.getDriver().getName() : null,
                fueling.getLiters(),
                fueling.getPricePerLiter(),
                fueling.getTotalCost(),
                fueling.getOdometer(),
                fueling.getFuelType(),
                fueling.getFuelStation(),
                fueling.getDescription()
        );
    }
}
