package io.dxheroes.endpointsmonitoringservice.service;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.UserDTO;
import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import io.dxheroes.endpointsmonitoringservice.entity.UserEntity;
import io.dxheroes.endpointsmonitoringservice.entity.mapper.UserMapper;
import io.dxheroes.endpointsmonitoringservice.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        validateUser(userDTO);
        UserEntity mappedEntity = userMapper.toEntity(userDTO);
        mappedEntity.setId(null);

        if(mappedEntity.getAccessToken() == null){
            mappedEntity.setAccessToken(UUID.randomUUID().toString());
        }

        if(userRepository.existsByAccessToken(userDTO.getAccessToken())){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("accessToken")
                    .invalidValue(userDTO.getAccessToken())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.ACCESS_TOKEN_ALREADY_EXISTS).build();
            throw new ValidationException(validationErrorDTO);
        }

        if(userRepository.existsByUsername(userDTO.getUsername())){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("username")
                    .invalidValue(userDTO.getUsername())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.USERNAME_ALREADY_EXISTS).build();
            throw new ValidationException(validationErrorDTO);
        }

        UserEntity saved = userRepository.save(mappedEntity);

        return userMapper.toDTO(saved);
    }

    public UserDTO editUser(UserDTO userDTO, String accessToken) {
        if(!userDTO.getAccessToken().equals(accessToken)){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("accessToken")
                    .invalidValue(userDTO.getAccessToken())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.CHANGE_OF_OTHER_USER).build();
            throw new ValidationException(validationErrorDTO);
        }
        Optional<UserEntity> userOptional = userRepository.getByAccessToken(accessToken);

        if(!userOptional.isPresent()){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("accessToken")
                    .invalidValue(userDTO.getAccessToken())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.USER_NOT_FOUND).build();
            throw new ValidationException(validationErrorDTO);
        }

        UserEntity user = userOptional.get();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        UserEntity saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    private void validateUser(UserDTO userDTO){
        List<ValidationErrorDTO> validationErrors = new ArrayList<>();

        if(userDTO.getUsername() == null || userDTO.getUsername().isEmpty()){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("username")
                    .invalidValue(userDTO.getUsername())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.INVALID_NAME).build();
            validationErrors.add(validationErrorDTO);
        }
        if(userDTO.getEmail() == null || !Utils.isValidEmail(userDTO.getEmail())){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("email")
                    .invalidValue(userDTO.getEmail())
                    .clazz(UserDTO.class)
                    .errorCode(ValidationErrorConstants.INVALID_EMAIL).build();
            validationErrors.add(validationErrorDTO);
        }
        if(!validationErrors.isEmpty()){
            throw new ValidationException(validationErrors);
        }
    }

    public void deleteByAccessToken(String accessToken) {
        userRepository.deleteByAccessToken(accessToken);
    }
}
