package com.managment.fleet.repository;

import com.managment.fleet.domain.PartsAutoMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PartsAutoMaintenanceRepository extends JpaRepository<PartsAutoMaintenance, String>, JpaSpecificationExecutor<PartsAutoMaintenance> {
}

