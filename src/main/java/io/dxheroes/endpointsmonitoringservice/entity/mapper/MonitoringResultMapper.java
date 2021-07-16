package io.dxheroes.endpointsmonitoringservice.entity.mapper;

import io.dxheroes.endpointsmonitoringservice.dto.MonitoredEndpointDTO;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoringResultDTO;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoringResultEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitoringResultMapper {

    MonitoringResultDTO toDTO(MonitoringResultEntity source);
    MonitoringResultEntity toEntity(MonitoringResultDTO source);

}
