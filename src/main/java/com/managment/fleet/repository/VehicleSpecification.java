package com.managment.fleet.repository;

import com.managment.fleet.domain.Vehicle;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification {

    public static Specification<Vehicle> licensePlateContains(String licensePlate) {
        return (root, query, cb) ->
                licensePlate == null ? null : cb.like(cb.upper(root.get("licensePlate")), "%" + licensePlate.toUpperCase() + "%");
    }

    public static Specification<Vehicle> modelContains(String model) {
        return (root, query, cb) ->
                model == null ? null : cb.like(cb.upper(root.get("model")), "%" + model.toUpperCase() + "%");
    }

    public static Specification<Vehicle> hasStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(cb.upper(root.get("status")), status.toUpperCase());
    }

    public static Specification<Vehicle> hasTypeOfCar(String typeofcar) {
        return (root, query, cb) ->
                typeofcar == null ? null : cb.equal(root.get("typeOfCar"), typeofcar);
    }
}