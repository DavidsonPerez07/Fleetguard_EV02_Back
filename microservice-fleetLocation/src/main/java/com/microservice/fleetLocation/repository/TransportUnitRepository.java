package com.microservice.fleetLocation.repository;
import com.microservice.fleetLocation.entity.TransportUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransportUnitRepository extends JpaRepository<TransportUnit, Long> {
    List<TransportUnit> findAllByDeletedFalse();
    Optional<TransportUnit> findByLicencePlate(String licencePlate);   
}
