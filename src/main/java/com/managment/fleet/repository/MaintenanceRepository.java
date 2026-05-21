package com.managment.fleet.repository;

import com.managment.fleet.domain.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, String> {
    boolean existsByVehicleId(String vehicleId);
    List<Maintenance> findByVehicleIdOrderByDateDesc(String vehicleId);
}
