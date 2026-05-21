package com.managment.fleet.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
@Setter
public class Vehicle {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @NotBlank(message = "License plate is mandatory")
    @Size(max = 10, message = "License plate must have at most 10 characters")
    @Column(name = "license_plate", nullable = false, unique = true, length = 10)
    private String licensePlate;

    @NotBlank(message = "Model is mandatory")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Manufacturer is mandatory")
    @Column(nullable = false)
    private String manufacturer;

    @NotNull(message = "Year is mandatory")
    // 'year' is a reserved word in some databases (H2). Map to a safe column name.
    @Column(name = "vehicle_year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, updatable = true)
    private VehicleTypeOfCar typeofcar;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "mileage",  nullable = false, updatable = true)
    private Double mileage;

    @Column(name = "nextMaintenanceMileage",  nullable = false, updatable = true)
    private Double nextMaintenanceMileage;

    @Column(name = "rentalValue", nullable = false, updatable = true)
    Double rentalValue;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum VehicleStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }

    public enum VehicleTypeOfCar {
        PROPRIO, ALUGADO
    }
}