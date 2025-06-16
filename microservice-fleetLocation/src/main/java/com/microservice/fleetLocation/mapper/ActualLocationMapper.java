package com.microservice.fleetLocation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.entity.ActualLocation;

@Mapper(componentModel = "spring")
public interface ActualLocationMapper {
    ActualLocationMapper INSTANCE = Mappers.getMapper(ActualLocationMapper.class);
    ActualLocationDTO toDTO(ActualLocation actualLocation);
    ActualLocation toEntity(ActualLocationDTO actualLocationDTO);
}
