package com.microservice.fleetLocation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.service.FleetLocationService;

@RestController
@RequestMapping("/api/fleetLocation/location")
public class FleetLocationController {
    private final FleetLocationService fleetLocationService;

    @Autowired
    public FleetLocationController(FleetLocationService fleetLocationService) {
        this.fleetLocationService = fleetLocationService;
    }

    @GetMapping(value = "/updates/{unitId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLocationUpdates(@PathVariable Long unitId) {
        return fleetLocationService.subscribeToLocationUpdates(unitId);
    }

    @GetMapping("/current/{unitId}")
    public ResponseEntity<ActualLocationDTO> getCurrentLocation(@PathVariable Long unitId) {
        return ResponseEntity.ok(fleetLocationService.getCurrentLocation(unitId));
    }
}
