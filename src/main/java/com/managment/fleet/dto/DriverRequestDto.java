package com.managment.fleet.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DriverRequestDto(
        @NotBlank String name,
        @NotBlank String cnh,
        @NotNull @Future LocalDate cnhExpirationDate
) {

    public String id() {
        throw new UnsupportedOperationException("Unimplemented method 'id'");
    }}