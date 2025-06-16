package com.microservice.fleetLocation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.fleetLocation.entity.ActualLocation;

@Repository
public interface ActualLocationRepository extends JpaRepository<ActualLocation, Long>{
    Optional<ActualLocation> findByTransportUnitId(Long transportUnitId);
    Optional<ActualLocation> findByTransportUnitLicencePlate(String licencePlate);
}
