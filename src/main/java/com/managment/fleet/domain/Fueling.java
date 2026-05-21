package com.managment.fleet.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fuelings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Fueling {

    @Id
    @Column(name = "fuelingid")
    private String fuelingId;

    @ManyToOne
    @JoinColumn(name = "vehicleid")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driverid")
    private Driver driver;

    @Column(name = "fueling_date")
    private LocalDate fuelingDate;

    @Column(name = "liters")
    private Double liters;

    @Column(name = "price_per_liter")
    private Double pricePerLiter;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "odometer")
    private Integer odometer;

    @Column(name = "fuel_station")
    private String fuelStation;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "description")
    private String description;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
