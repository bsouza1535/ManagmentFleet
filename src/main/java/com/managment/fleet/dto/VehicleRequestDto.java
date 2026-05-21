package com.managment.fleet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleRequestDto(

        @NotBlank(message = "License plate is required")
        @Size(max = 10, message = "License plate must have at most 10 characters")
        String licensePlate,

        @NotBlank(message = "Model is required")
        String model,

        @NotBlank(message = "Manufacturer is required")
        String manufacturer,

        @NotNull(message = "Year is required")
        Integer year,

        @NotBlank(message = "Status is Required")
        String status,

        //@NotBlank(message = "Type of car is required")
        String typeofcar,

        @NotNull(message = "Mileage of car is required")
        Double mileage,

        @NotNull(message = "NextMaintenanceMileage of car is required")
        Double nextMaintenanceMileage,

        //@NotNull(message = "Rental Value of car is required")
        Double rentalValue

) {}

