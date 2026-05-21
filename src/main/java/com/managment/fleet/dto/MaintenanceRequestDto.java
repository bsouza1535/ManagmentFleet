package com.managment.fleet.dto;

import com.managment.fleet.domain.Maintenance;
import com.managment.fleet.domain.MaintenanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record MaintenanceRequestDto(

        @NotBlank(message = "License plate is required")
        @Size(max = 10, message = "License plate must have at most 10 characters")
        String licensePlate,

        @NotBlank(message = "Description is required")
        @Size(max = 500, message = "Description must have at most 500 characters")
        String description,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Parts Auto are required")
        List<Maintenance.AutoPart> partsAuto,

        @NotNull(message = "Repairs auto is required")
        String repairs,

        @NotNull(message = "Price of the maintanence is required")
        Double priceOfMaintanence,

        @NotNull(message = "mileage of the next maintanence is required")
        Double next_maintenance_mileage,

        @NotNull(message = "mileage of maintanence is required")
        Double mileageOfMaintanence,

        @NotNull(message = "Type of maintanence is required")
        String typeOfMaintenance,

        MaintenanceStatus status  // Opcional - se null, será SCHEDULED por padrão

) {}

