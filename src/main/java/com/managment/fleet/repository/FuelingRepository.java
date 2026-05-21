package com.managment.fleet.repository;

import com.managment.fleet.domain.Fueling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FuelingRepository extends JpaRepository<Fueling, String> {
    boolean existsByVehicleId(String vehicleId);

    @Query("""
        SELECT FUNCTION('TO_CHAR', f.fuelingDate, 'Mon') as month,
               SUM(f.totalCost) as totalCost
        FROM Fueling f
        WHERE f.fuelingDate >= :startDate
        GROUP BY FUNCTION('TO_CHAR', f.fuelingDate, 'Mon'),
                 FUNCTION('TO_CHAR', f.fuelingDate, 'YYYY-MM')
        ORDER BY FUNCTION('TO_CHAR', f.fuelingDate, 'YYYY-MM')
    """)
    List<Object[]> findMonthlyCosts(@Param("startDate") LocalDate startDate);

    @Query("SELECT SUM(f.totalCost) FROM Fueling f WHERE f.fuelingDate >= :startDate")
    Double sumCostsSince(@Param("startDate") LocalDate startDate);
}
