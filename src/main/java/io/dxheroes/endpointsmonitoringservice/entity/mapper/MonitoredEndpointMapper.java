package io.dxheroes.endpointsmonitoringservice.entity.mapper;

import io.dxheroes.endpointsmonitoringservice.dto.MonitoredEndpointDTO;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MonitoredEndpointMapper {

    @Mapping(target = "monitoredIntervalInMillis", source = "monitoredInterval")
    MonitoredEndpointDTO toDTO(MonitoredEndpointEntity source);
    @Mapping(target = "monitoredInterval", source = "monitoredIntervalInMillis")
    MonitoredEndpointEntity toEntity(MonitoredEndpointDTO source);

}
