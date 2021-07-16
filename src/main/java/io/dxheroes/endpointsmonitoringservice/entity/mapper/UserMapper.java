package io.dxheroes.endpointsmonitoringservice.entity.mapper;

import io.dxheroes.endpointsmonitoringservice.dto.UserDTO;
import io.dxheroes.endpointsmonitoringservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity source);
    UserEntity toEntity(UserDTO source);

}