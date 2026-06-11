package com.managment.fleet.controller;

import com.managment.fleet.dto.FuelingRequestDto;
import com.managment.fleet.dto.FuelingResponseDto;
import com.managment.fleet.service.FuelingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuelings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FuelingController {

    private final FuelingService fuelingService;

    @PostMapping
    public ResponseEntity<FuelingResponseDto> createFueling(@RequestBody FuelingRequestDto dto) {
        FuelingResponseDto response = fuelingService.createFueling(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<FuelingResponseDto>> getAllFuelings(
            @PageableDefault(size = 100, sort = "fuelingDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FuelingResponseDto> fuelings = fuelingService.findAll(pageable);
        return ResponseEntity.ok(fuelings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelingResponseDto> getFuelingById(@PathVariable String id) {
        FuelingResponseDto fueling = fuelingService.findById(id);
        return ResponseEntity.ok(fueling);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelingResponseDto> updateFueling(
            @PathVariable String id,
            @RequestBody FuelingRequestDto dto
    ) {
        FuelingResponseDto response = fuelingService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFueling(@PathVariable String id) {
        fuelingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
