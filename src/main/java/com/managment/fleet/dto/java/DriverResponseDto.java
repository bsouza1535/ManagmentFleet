package com.managment.fleet.dto.java;


import com.managment.fleet.domain.Driver;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DriverResponseDto(
        String name,
        String cnh,
        LocalDate cnhExpirationDate,
        Driver.DriverStatus status,
        LocalDateTime createdAt
) {
    public static DriverResponseDto from(Driver driver) {
        return new DriverResponseDto(
                driver.getName(),
                driver.getCnh(),
                driver.getCnhExpirationDate(),
                driver.getStatus(),
                driver.getCreatedAt()
        );
    }
}
