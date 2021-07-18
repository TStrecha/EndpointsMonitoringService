package io.dxheroes.endpointsmonitoringservice.integration;

import io.dxheroes.endpointsmonitoringservice.constant.Status;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoredEndpointDTO;
import io.dxheroes.endpointsmonitoringservice.dto.UserDTO;
import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import io.dxheroes.endpointsmonitoringservice.entity.UserEntity;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoredEndpointRepository;
import io.dxheroes.endpointsmonitoringservice.entity.repository.UserRepository;
import io.dxheroes.endpointsmonitoringservice.service.MonitoredEndpointService;
import io.dxheroes.endpointsmonitoringservice.service.MonitoringResultService;
import io.dxheroes.endpointsmonitoringservice.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public static final String ACCESS_TOKEN = "dcb20f8a-5657-4f1b-9f7f-ce65739b359e";

    @Test
    @Order(0)
    void user_createNew_success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAccessToken(ACCESS_TOKEN);
        userDTO.setUsername("Unit test");
        userDTO.setEmail("test@example.com");

        UserDTO result = userService.createUser(userDTO);

        assertEquals(result.getAccessToken(), ACCESS_TOKEN);
        assertEquals(result.getEmail(), userDTO.getEmail());
        assertEquals(result.getUsername(), userDTO.getUsername());
        assertNotNull(result.getId());
    }

    @Test
    @Order(1)
    void user_get_success() {
        UserEntity user = createUser();

        UserDTO result = userService.getUserByAccessToken(ACCESS_TOKEN);

        assertEquals(result.getAccessToken(), ACCESS_TOKEN);
        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getUsername(), user.getUsername());
        assertNotNull(result.getId());
    }

    @Test
    @Order(2)
    void user_get_fail() {
        UserEntity user = createUser();

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.getUserByAccessToken("defdc532-be06-44d0-ba58-e5aeb1253ca4") );

        assertEquals(1, exception.getErrors().size());
        assertEquals(ValidationErrorConstants.USER_NOT_FOUND, exception.getErrors().get(0).getErrorCode());

    }

    @Test
    @Order(3)
    void user_createNew_fail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Unit test 2");
        userDTO.setEmail("test2.c");
        boolean failed = false;

        try{
            userService.createUser(userDTO);
        } catch (ValidationException ex){
            assertEquals(1, ex.getErrors().size());
            assertEquals(ValidationErrorConstants.INVALID_EMAIL, ex.getErrors().get(0).getErrorCode());
            failed = true;
        }
        assertTrue(failed);
    }

    @Test
    @Order(4)
    void user_edit_success() {
        UserEntity user = createUser();
        UserDTO userDTO = new UserDTO();
        userDTO.setAccessToken(ACCESS_TOKEN);
        userDTO.setUsername("Unit test edited");
        userDTO.setEmail("editedtest@example.com");

        userDTO = userService.editUser(userDTO, ACCESS_TOKEN);

        assertEquals(userDTO.getAccessToken(), ACCESS_TOKEN);
        assertEquals(userDTO.getEmail(), userDTO.getEmail());
        assertEquals(userDTO.getUsername(), userDTO.getUsername());
        assertNotNull(userDTO.getId());
    }

    @Test
    @Order(5)
    void user_edit_fail() {
        UserEntity user = createUser();
        UserDTO userDTO = new UserDTO();
        userDTO.setAccessToken(ACCESS_TOKEN);
        userDTO.setUsername("Unit test second edit");

        try {
            userDTO = userService.editUser(userDTO, "93f39e2f-80de-4033-99ee-249d92736a20");
        } catch (ValidationException ex){
            assertEquals(1, ex.getErrors().size());
            assertEquals(ValidationErrorConstants.CHANGE_OF_OTHER_USER, ex.getErrors().get(0).getErrorCode());
        }
    }

    private UserEntity createUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setAccessToken(ACCESS_TOKEN);
        userEntity.setUsername("Unit test");
        userEntity.setEmail("test@example.com");

        return userRepository.saveAndFlush(userEntity);
    }



}
