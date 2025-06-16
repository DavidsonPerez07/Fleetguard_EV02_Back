package com.microservice.fleetLocation.mapper;  
import org.mapstruct.Mapper;
import com.microservice.fleetLocation.entity.Fleet;
import com.microservice.fleetLocation.DTO.FleetDTO;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface FleetMapper {
    FleetMapper INSTANCE = Mappers.getMapper(FleetMapper.class);
    FleetDTO toDTO(Fleet fleet);
    Fleet toEntity(FleetDTO fleetDTO);
}


