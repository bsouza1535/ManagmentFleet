package com.managment.fleet.dto;

import com.managment.fleet.domain.Maintenance;
import com.managment.fleet.domain.MaintenanceStatus;

import java.time.LocalDate;
import java.util.List;

public record MaintenanceResponseDto(
        String id,
        String licensePlate,
        String vehicleModel,
        String description,
        LocalDate date,
        List<Maintenance.AutoPart> partsAuto,
        String typeofmaintenance,
        String repairs,
        Double priceOfMaintanence,
        Double mileageOfMaintanence,
        Double next_maintenance_mileage,
        MaintenanceStatus status
) {
    public static MaintenanceResponseDto from(Maintenance maintenance) {
        return new MaintenanceResponseDto(
                maintenance.getId(),
                maintenance.getVehicle().getLicensePlate(),
                maintenance.getVehicle().getModel(),
                maintenance.getDescription(),
                maintenance.getDate(),
                maintenance.getPartsAuto(),
                maintenance.getTypeOfMaintenance().name(),
                maintenance.getAutorepair(),
                maintenance.getKilometersOfMaintenance(),
                maintenance.getNext_maintenance_mileage(),
                maintenance.getPriceOfMaintanence(),
                maintenance.getStatus()
        );
    }
}