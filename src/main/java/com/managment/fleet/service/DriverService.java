package com.managment.fleet.service;

import com.managment.fleet.domain.Driver;
import com.managment.fleet.dto.DriverRequestDto;
import com.managment.fleet.dto.DriverResponseDto;
import com.managment.fleet.exception.BusinessException;
import com.managment.fleet.repository.DriverRepository;
import com.managment.fleet.repository.DriverSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository repository;

    @Transactional
    public DriverResponseDto create(DriverRequestDto dto) {
        if (dto.cnh() == null || dto.cnh().isBlank()) {
            throw new BusinessException("CNH is required.");
        }

        if (repository.existsByCnh(dto.cnh())) {
            throw new BusinessException("Driver with CNH '" + dto.cnh() + "' already exists.");
        }

        if (dto.cnhExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Driver CNH is expired.");
        }

        Driver driver = new Driver();
        driver.setName(dto.name());
        driver.setCnh(dto.cnh());
        driver.setCnhExpirationDate(dto.cnhExpirationDate());

        Driver saved = repository.save(driver);

        return new DriverResponseDto(
                saved.getName(),
                saved.getCnh(),
                saved.getCnhExpirationDate(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public Page<DriverResponseDto> findAllFiltered(String name, String cnh, String status, Pageable pageable) {
        Specification<Driver> spec = Specification
                .where(DriverSpecification.nameContains(name))
                .and(DriverSpecification.cnhEquals(cnh))
                .and(DriverSpecification.hasStatus(status));

        return repository.findAll(spec, pageable)
                .map(d -> new DriverResponseDto(
                        d.getName(),
                        d.getCnh(),
                        d.getCnhExpirationDate(),
                        d.getStatus(),
                        d.getCreatedAt()
                ));
    }

    @Transactional
    public DriverResponseDto update(DriverRequestDto dto) {
        Driver driver = repository.findById(dto.id())
                .orElseThrow(() -> new BusinessException("Driver with ID '" + dto.id() + "' not found."));

        if (dto.cnh() == null || dto.cnh().isBlank()) {
            throw new BusinessException("CNH is required.");
        }

        if (!driver.getCnh().equals(dto.cnh()) && repository.existsByCnh(dto.cnh())) {
            throw new BusinessException("Driver with CNH '" + dto.cnh() + "' already exists.");
        }

        if (dto.cnhExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Driver CNH is expired.");
        }

        driver.setName(dto.name());
        driver.setCnh(dto.cnh());
        driver.setCnhExpirationDate(dto.cnhExpirationDate());

        Driver updated = repository.save(driver);

        return new DriverResponseDto(
                updated.getName(),
                updated.getCnh(),
                updated.getCnhExpirationDate(),
                updated.getStatus(),
                updated.getCreatedAt()
        );
    }
}
