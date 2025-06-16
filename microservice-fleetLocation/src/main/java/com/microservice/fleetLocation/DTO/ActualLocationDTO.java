package com.microservice.fleetLocation.DTO;

import java.time.LocalDateTime;

import com.microservice.fleetLocation.entity.TransportUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActualLocationDTO {
    private Long id;

    private Float latitude;

    private Float longitude;

    private Float speed;

    private LocalDateTime timestamp;

    private TransportUnit transportUnit;
}
