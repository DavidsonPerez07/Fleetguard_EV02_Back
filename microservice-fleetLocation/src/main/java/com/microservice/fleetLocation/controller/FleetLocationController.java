package com.microservice.fleetLocation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.service.LocationTransportUnitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Transport Unit Location", description = "Endpoints para optener la ubicacion de las unidades de transporte")
@RestController
@RequestMapping("/api/fleetLocation/location")
public class FleetLocationController {
    private final LocationTransportUnitService fleetLocationService;

    @Autowired
    public FleetLocationController(LocationTransportUnitService fleetLocationService) {
        this.fleetLocationService = fleetLocationService;
    }
    @Operation(summary = "Actualizar ubicación", description = "Actualiza la ubicación de una unidad de transporte")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR') or hasRole('DRIVER')")
    @PostMapping(value = "/updates/{licencePlate}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable String licencePlate,
            @RequestBody ActualLocationDTO actualLocationDTO) {

        fleetLocationService.saveActualLocation(actualLocationDTO, licencePlate);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Obtener ubicación actual", description = "Obtiene la ubicación actual de todas las unidades de transporte ")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR') or hasRole('DRIVER')")
    @GetMapping("/current/all")
    public ResponseEntity<List<ActualLocationDTO>> getAllLocation() {
        return ResponseEntity.ok(fleetLocationService.getAllLocation());
    }

    
    @Operation(summary = "Obtener ubicación por matrícula", description = "Obtiene la ubicación actual de una unidad de transporte por su matrícula")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR') or hasRole('DRIVER')")
    @GetMapping("/current/{licencePlate}")
    public ResponseEntity<ActualLocationDTO> getLocationByLicencePlate(@PathVariable String licencePlate) {
        try {
            ActualLocationDTO locationDTO = fleetLocationService.getLocationByLicencePlate(licencePlate);
            return ResponseEntity.ok(locationDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
