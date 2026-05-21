package com.managment.fleet.repository;

import com.managment.fleet.domain.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, String>, JpaSpecificationExecutor<Driver> {
    boolean existsByCnh(String cnh);
    Optional<Driver> findByCnh(String cnh);

    Page<Driver> findAll(Specification<Driver> spec, Pageable pageable);

    Optional<Driver> findByNameIgnoreCase(String driverName);
}
