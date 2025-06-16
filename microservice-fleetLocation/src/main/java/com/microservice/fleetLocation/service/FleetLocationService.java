package com.microservice.fleetLocation.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.entity.ActualLocation;
import com.microservice.fleetLocation.entity.TransportUnit;
import com.microservice.fleetLocation.mapper.ActualLocationMapper;
import com.microservice.fleetLocation.repository.ActualLocationRepository;
import com.microservice.fleetLocation.repository.TransportUnitRepository;

import jakarta.annotation.PostConstruct;

@Service
public class FleetLocationService {
    @Autowired
    private final TransportUnitRepository transportUnitRepository;
    private final ActualLocationRepository actualLocationRepository;
    private final ActualLocationMapper actualLocationMapper;
    private TransportUnit unit;

    public FleetLocationService(TransportUnitRepository transportUnitRepository, ActualLocationRepository actualLocationRepository, 
    ActualLocationMapper actualLocationMapper) {
        this.transportUnitRepository = transportUnitRepository;
        this.actualLocationMapper = actualLocationMapper;
        this.actualLocationRepository = actualLocationRepository;
    }

    @PostConstruct
    public void init() {
        this.unit = transportUnitRepository.findById(1L)
            .orElseThrow(() -> new IllegalStateException("TransportUnit with ID 1 not found on application startup."));
    }

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    

    private static final Float MEDELLIN_LAT = 6.2476f;
    private static final Float MEDELLIN_LONG = -75.5658f;
    private static final Float RADIUS = 0.1f; // 10 km

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS) // 15000 ms = 15 s
    @Transactional
    public void updateFleetLocation() {
        ActualLocation newLocation = generateSimulatedLocation(unit);

        notifySubscribers(unit.getId(), newLocation);
    }

    private void notifySubscribers(Long unitId, ActualLocation location) {
        SseEmitter emitter = emitters.get(unitId);

        if (emitter != null) {
            try {
                emitter.send(actualLocationMapper.toDTO(location));
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(unitId);
            }
        }
    }

    private ActualLocation generateSimulatedLocation(TransportUnit unit) {
        Float randomLat = MEDELLIN_LAT + (ThreadLocalRandom.current().nextFloat() * 2 * RADIUS - RADIUS);
        Float randomLong = MEDELLIN_LONG + (ThreadLocalRandom.current().nextFloat() * 2 * RADIUS - RADIUS);

        ActualLocation location;
        Optional<ActualLocation> existingLocationOptional = actualLocationRepository.findById(1L);

        if (existingLocationOptional.isPresent()) {
            location = existingLocationOptional.get();
            location.setLatitude(randomLat);
            location.setLongitude(randomLong);
            location.setSpeed(ThreadLocalRandom.current().nextFloat(0, 100)); // Speed between 0 - 100 km/h
            location.setTimestamp(LocalDateTime.now());
        } else {
            location = new ActualLocation();
            location.setId(1L);
            location.setLatitude(randomLat);
            location.setLongitude(randomLong);
            location.setSpeed(ThreadLocalRandom.current().nextFloat(0, 100)); // Speed between 0 - 100 km/h
            location.setTimestamp(LocalDateTime.now());
            location.setTransportUnit(unit);
        }

        return actualLocationRepository.save(location);
    }

    public SseEmitter subscribeToLocationUpdates(Long unitId) {
        SseEmitter emitter = new SseEmitter(3600000L);
        emitters.put(unitId, emitter);

        actualLocationRepository.findByTransportUnitId(unitId).ifPresent(location -> {
            try {
                emitter.send(actualLocationMapper.toDTO(location));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        emitter.onCompletion(() -> emitters.remove(unitId));
        emitter.onTimeout(() -> emitters.remove(unitId));

        return emitter;
    }

    public ActualLocationDTO getCurrentLocation(Long unitId) {
        return actualLocationRepository.findByTransportUnitId(unitId)
            .map(actualLocationMapper::toDTO)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "No se encontró ubicación para la unidad " + unitId));
    }
}
