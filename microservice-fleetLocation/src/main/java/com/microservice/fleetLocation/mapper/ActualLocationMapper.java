package com.microservice.fleetLocation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.microservice.fleetLocation.DTO.ActualLocationDTO;
import com.microservice.fleetLocation.entity.ActualLocation;
import com.microservice.fleetLocation.entity.TransportUnit;

@Mapper(componentModel = "spring")
public interface ActualLocationMapper {
    ActualLocationMapper INSTANCE = Mappers.getMapper(ActualLocationMapper.class);

    ActualLocationDTO toDTO(ActualLocation actualLocation);
    
    @Mapping(target = "transportUnit", expression = "java(mapLicencePlateToTransportUnit(actualLocationDTO.getTransportUnit()))")
    ActualLocation toEntity(ActualLocationDTO actualLocationDTO);

    default String mapTransportUnitToString(TransportUnit transportUnit) {
        return transportUnit != null ? transportUnit.getLicencePlate() : null;
    }

    default TransportUnit mapLicencePlateToTransportUnit(String licencePlate) {
        if (licencePlate == null) return null;
        TransportUnit tu = new TransportUnit();
        tu.setLicencePlate(licencePlate);
        return tu;
    }
}
