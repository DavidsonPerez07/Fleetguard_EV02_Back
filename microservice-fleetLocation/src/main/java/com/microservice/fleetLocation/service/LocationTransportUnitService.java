package com.microservice.fleetLocation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.entity.ActualLocation;
import com.microservice.fleetLocation.entity.TransportUnit;
import com.microservice.fleetLocation.mapper.ActualLocationMapper;
import com.microservice.fleetLocation.repository.ActualLocationRepository;
import com.microservice.fleetLocation.repository.TransportUnitRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class LocationTransportUnitService {

    private final ActualLocationMapper actualLocationMapper;
    private final ActualLocationRepository actualLocationRepository;
    private final TransportUnitRepository transportUnitRepository;

    public LocationTransportUnitService(ActualLocationMapper actualLocationMapper,
                                         ActualLocationRepository actualLocationRepository,
                                         TransportUnitRepository transportUnitRepository) {
        this.actualLocationMapper = actualLocationMapper;
        this.actualLocationRepository = actualLocationRepository;
        this.transportUnitRepository = transportUnitRepository;
    }


    public ActualLocationDTO saveActualLocation(ActualLocationDTO actualLocationDTO, String licencePlate) {
        // Buscar la unidad de transporte
        TransportUnit transportUnit = transportUnitRepository.findByLicencePlate(licencePlate)
                .orElseThrow(() -> new EntityNotFoundException("Transport unit not found with licence plate: " + licencePlate));

        // Buscar ubicación existente para esta unidad
        ActualLocation location = actualLocationRepository.findByTransportUnitLicencePlate(licencePlate)
                .orElseGet(ActualLocation::new); 
        
        location.setTransportUnit(transportUnit);
        location.setLatitude(actualLocationDTO.getLatitude());
        location.setLongitude(actualLocationDTO.getLongitude());
        location.setSpeed(actualLocationDTO.getSpeed());
        location.setTimestamp(LocalDateTime.now());
       

        ActualLocation saved = actualLocationRepository.save(location);

    return actualLocationMapper.toDTO(saved);
}

    

    //Get all Location
    public List<ActualLocationDTO> getAllLocation() {
        return actualLocationRepository.findAll().stream().map(actualLocationMapper::toDTO).toList();
    }

    public ActualLocationDTO getLocationByLicencePlate(String licencePlate) {
    // Buscar ubicación por placa
    ActualLocation location = actualLocationRepository.findByTransportUnitLicencePlate(licencePlate)
            .orElseThrow(() -> new EntityNotFoundException("No location found for licence plate: " + licencePlate));

    return actualLocationMapper.toDTO(location);
}
}
