package com.microservice.fleetLocation.mapper;

import org.mapstruct.Mapper;
import com.microservice.fleetLocation.entity.TransportUnit;
import com.microservice.fleetLocation.DTO.TransportUnitDTO;
import com.microservice.fleetLocation.DTO.TransportUnitDetailDTO;


import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
    UserMapper.class,
    FleetMapper.class,
    TransportUnitStatusMapper.class
})

public interface TransportUnitMapper {
    TransportUnitMapper INSTANCE = Mappers.getMapper(TransportUnitMapper.class);

    TransportUnitDTO toDTO(TransportUnit transportUnit);
    TransportUnitDetailDTO toDetailDTO(TransportUnit transportUnit);
    TransportUnit toEntity(TransportUnitDTO transportUnitDTO);  
}

