package io.dxheroes.endpointsmonitoringservice.service;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.EntityNotFoundException;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoredEndpointDTO;
import io.dxheroes.endpointsmonitoringservice.dto.StatusDTO;
import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import io.dxheroes.endpointsmonitoringservice.entity.UserEntity;
import io.dxheroes.endpointsmonitoringservice.entity.mapper.MonitoredEndpointMapper;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoredEndpointRepository;
import io.dxheroes.endpointsmonitoringservice.entity.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class MonitoredEndpointService {

    @Autowired
    private MonitoredEndpointMapper monitoredEndpointMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;

    @Autowired
    private MonitoringResultService monitoringResultService;

    public MonitoredEndpointDTO createMonitoredEndpoint(MonitoredEndpointDTO monitoredEndpointDTO, String accessToken) {
        Optional<UserEntity> userEntityOptional = userRepository.getByAccessToken(accessToken);
        if(!userEntityOptional.isPresent()){
            throw new EntityNotFoundException(UserEntity.class, "Access Token", accessToken);
        }

        validateMonitoredEndpoint(monitoredEndpointDTO);

        if(monitoredEndpointRepository.existsByName(monitoredEndpointDTO.getName())){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("name")
                    .invalidValue(monitoredEndpointDTO.getName())
                    .clazz(MonitoredEndpointDTO.class)
                    .errorCode(ValidationErrorConstants.NAME_ALREADY_EXISTS).build();
            throw new ValidationException(validationErrorDTO);
        }

        MonitoredEndpointEntity monitoredEndpoint = monitoredEndpointMapper.toEntity(monitoredEndpointDTO);
        monitoredEndpoint.setId(null);
        monitoredEndpoint.setOwner(userEntityOptional.get());
        MonitoredEndpointEntity saved = monitoredEndpointRepository.save(monitoredEndpoint);

        monitoringResultService.startTask(saved);

        return monitoredEndpointMapper.toDTO(saved);
    }

    private void validateMonitoredEndpoint(MonitoredEndpointDTO monitoredEndpointDTO){
        List<ValidationErrorDTO> validationErrors = new ArrayList<>();

        if(monitoredEndpointDTO.getMonitoredIntervalInMillis() == null || monitoredEndpointDTO.getMonitoredIntervalInMillis() <= 0){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("monitoredIntervalInMillis")
                    .invalidValue(monitoredEndpointDTO.getMonitoredIntervalInMillis())
                    .clazz(MonitoredEndpointDTO.class)
                    .errorCode(ValidationErrorConstants.INVALID_INTERVAL).build();
            validationErrors.add(validationErrorDTO);
        }
        if(monitoredEndpointDTO.getUrl() == null || !Utils.isValidURL(monitoredEndpointDTO.getUrl())){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("url")
                    .invalidValue(monitoredEndpointDTO.getUrl())
                    .clazz(MonitoredEndpointDTO.class)
                    .errorCode(ValidationErrorConstants.INVALID_URL).build();
            validationErrors.add(validationErrorDTO);
        }
        if(monitoredEndpointDTO.getName() == null || monitoredEndpointDTO.getName().isEmpty()){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("name")
                    .invalidValue(monitoredEndpointDTO.getName())
                    .clazz(MonitoredEndpointDTO.class)
                    .errorCode(ValidationErrorConstants.INVALID_NAME).build();
            validationErrors.add(validationErrorDTO);
        }
        if(!validationErrors.isEmpty()){
            throw new ValidationException(validationErrors);
        }
    }

    public StatusDTO deleteMonitoredEndpoint(Long monitoredEndpointId, String accessToken) {
        MonitoredEndpointEntity entity = monitoredEndpointRepository.getForAccessToken(monitoredEndpointId, accessToken);

        if(entity == null){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("monitoredEndpointId")
                    .invalidValue(monitoredEndpointId)
                    .errorCode(ValidationErrorConstants.NO_ENTITY_FOUND_FOR_ACCESS_TOKEN).build();
            throw new ValidationException(validationErrorDTO);
        }
        monitoringResultService.stopTask(entity);
        monitoredEndpointRepository.delete(entity);
        return new StatusDTO(StatusDTO.Status.SUCCESS);
    }

    public List<MonitoredEndpointDTO> getMonitoredEndpointsForAccessToken(String accessToken) {
        return monitoredEndpointRepository.getAllForAccessToken(accessToken).stream().map(monitoredEndpointMapper::toDTO).collect(Collectors.toList());
    }

    public MonitoredEndpointDTO editMonitoredEndpoint(Long monitoredEndpointId, MonitoredEndpointDTO monitoredEndpointDTO, String accessToken) {
        MonitoredEndpointEntity entity = monitoredEndpointRepository.getForAccessToken(monitoredEndpointId, accessToken);

        if(entity == null){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("monitoredEndpointId")
                    .invalidValue(monitoredEndpointId)
                    .errorCode(ValidationErrorConstants.NO_ENTITY_FOUND_FOR_ACCESS_TOKEN).build();
            throw new ValidationException(validationErrorDTO);
        }

        validateMonitoredEndpoint(monitoredEndpointDTO);

        entity.setMonitoredInterval(monitoredEndpointDTO.getMonitoredIntervalInMillis());
        entity.setName(monitoredEndpointDTO.getName());
        entity.setUrl(monitoredEndpointDTO.getUrl());
        if(!entity.getUrl().equals(monitoredEndpointDTO.getUrl())){
            monitoringResultService.stopTask(entity);
            monitoringResultService.startTask(entity);
        }

        MonitoredEndpointEntity saved = monitoredEndpointRepository.save(entity);

        return monitoredEndpointMapper.toDTO(saved);
    }

}
