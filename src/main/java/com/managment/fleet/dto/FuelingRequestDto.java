package com.managment.fleet.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record FuelingRequestDto(
        @NotBlank(message = "License plate is required")
        @Size(max = 10, message = "License plate must have at most 10 characters")
        String licensePlate,

        String driverName,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Liters is required")
        @Positive(message = "Liters must be positive")
        Double liters,

        @NotNull(message = "Price per liter is required")
        @Positive(message = "Price per liter must be positive")
        Double pricePerLiter,

        @NotNull(message = "Total cost is required")
        @Positive(message = "Total cost must be positive")
        Double totalCost,

        Integer odometer,

        String fuelType,

        String fuelStation,

        String description
) {
}

