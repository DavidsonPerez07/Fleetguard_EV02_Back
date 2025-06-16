package com.microservice.fleetLocation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.microservice.fleetLocation.DTO.UserDTO;
import com.microservice.fleetLocation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Driver", description = "Endpoints para gestionar conductores")
@RestController
@RequestMapping("/api/fleetLocation/user")
public class UserController {

    private final UserService userFacade;

    public UserController(UserService userFacade) {
        this.userFacade = userFacade;
    }

    // Get all users
    @Operation(summary = "Obtener conductores", description = "Se obtienen en una lista todos los conductores")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userFacade.getAllUsers());
    }
}
