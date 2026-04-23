package com.managment.fleet.repository;

import com.managment.fleet.domain.Driver;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecification {

    public static Specification<Driver> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank() ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Driver> cnhEquals(String cnh) {
        return (root, query, cb) ->
                cnh == null || cnh.isBlank() ? null :
                        cb.equal(root.get("cnh"), cnh);
    }

    public static Specification<Driver> hasStatus(String status) {
        return (root, query, cb) ->
                status == null || status.isBlank() ? null :
                        cb.equal(root.get("status"), Driver.DriverStatus.valueOf(status.toUpperCase()));
    }
}
