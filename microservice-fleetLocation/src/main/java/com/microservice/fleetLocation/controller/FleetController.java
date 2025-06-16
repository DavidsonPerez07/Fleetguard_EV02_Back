package com.microservice.fleetLocation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.microservice.fleetLocation.DTO.FleetDTO;
import com.microservice.fleetLocation.service.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "Fleets", description = "Endpoints para gestionar flotas")
@RestController
@RequestMapping("/api/fleetLocation/fleet")
public class FleetController {
    
    private final FleetService fleetFacade;

    public FleetController(FleetService fleetFacade) {
        this.fleetFacade = fleetFacade;
    }

    // Get all fleets
    @Operation(summary = "Obtener flotas", description = "Se obtienen en una lista todas las flotas registradas")
    @GetMapping
    public ResponseEntity<List<FleetDTO>> getAllFleets() {
        return ResponseEntity.ok(fleetFacade.getAllFleets());
    }

}

