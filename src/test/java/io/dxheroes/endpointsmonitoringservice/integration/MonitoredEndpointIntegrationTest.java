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
public class MonitoredEndpointIntegrationTest {

    @Autowired
    private MonitoredEndpointService monitoredEndpointService;
    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;
    @Autowired
    private MonitoringResultService monitoringResultService;
    @Autowired
    private UserRepository userRepository;

    private Integer taskCount = 0;

    @Test
    @Order(0)
    void endpoint_createNew_success() {
        UserEntity user = createUser();
        taskCount = monitoredEndpointService.getMonitoringResultService().getTasks().size();
        MonitoredEndpointDTO monitoredEndpointDTO = new MonitoredEndpointDTO();
        monitoredEndpointDTO.setName("Test endpoint");
        monitoredEndpointDTO.setUrl("http://www.example.com");
        monitoredEndpointDTO.setStatus(Status.ACTIVE);
        monitoredEndpointDTO.setMonitoredIntervalInMillis(30 * 1000);

        MonitoredEndpointDTO result = monitoredEndpointService.createMonitoredEndpoint(monitoredEndpointDTO, UserIntegrationTest.ACCESS_TOKEN);

        assertNotNull(result.getId());
        assertNotNull(result.getOwner());
        assertEquals(result.getOwner().getAccessToken(), UserIntegrationTest.ACCESS_TOKEN);
        assertEquals(result.getUrl(), monitoredEndpointDTO.getUrl());
        assertEquals(result.getMonitoredIntervalInMillis(), monitoredEndpointDTO.getMonitoredIntervalInMillis());
        assertEquals(result.getName(), monitoredEndpointDTO.getName());
        assertEquals(result.getStatus(), monitoredEndpointDTO.getStatus());
        assertEquals(taskCount + 1, monitoredEndpointService.getMonitoringResultService().getTasks().size());
        taskCount = monitoredEndpointService.getMonitoringResultService().getTasks().size();
    }

    @Test
    @Order(1)
    void endpoint_createNew_fail() {
        UserEntity user = createUser();
        MonitoredEndpointDTO monitoredEndpointDTO = new MonitoredEndpointDTO();
        monitoredEndpointDTO.setName("Test endpoint 2");
        monitoredEndpointDTO.setUrl("ht:/example.kz");

        boolean failed = false;

        try {
            monitoredEndpointService.createMonitoredEndpoint(monitoredEndpointDTO, UserIntegrationTest.ACCESS_TOKEN);
        } catch (ValidationException ex){
            assertEquals(2, ex.getErrors().size());
            for(ValidationErrorDTO error : ex.getErrors()){
                assertFalse(error.getErrorCode() != ValidationErrorConstants.INVALID_URL && error.getErrorCode() != ValidationErrorConstants.INVALID_INTERVAL);
            }
            failed = true;
        }

        assertTrue(failed);
    }

    @Test
    @Order(2)
    void endpoint_edit_success() {
        MonitoredEndpointEntity monitoredEndpointEntity = createMonitoredEndpoint();
        MonitoredEndpointDTO monitoredEndpointDTO = new MonitoredEndpointDTO();
        monitoredEndpointDTO.setName("Test endpoint edited");
        monitoredEndpointDTO.setUrl("https://reqres.in/api/unknown/2");
        monitoredEndpointDTO.setStatus(Status.INACTIVE);
        monitoredEndpointDTO.setMonitoredIntervalInMillis(50 * 1000);

        MonitoredEndpointDTO result = monitoredEndpointService.editMonitoredEndpoint(monitoredEndpointEntity.getId(), monitoredEndpointDTO, UserIntegrationTest.ACCESS_TOKEN);

        assertNotNull(result.getId());
        assertNotNull(result.getOwner());
        assertEquals(result.getOwner().getAccessToken(), UserIntegrationTest.ACCESS_TOKEN);
        assertEquals(result.getUrl(), monitoredEndpointDTO.getUrl());
        assertEquals(result.getMonitoredIntervalInMillis(), monitoredEndpointDTO.getMonitoredIntervalInMillis());
        assertEquals(result.getName(), monitoredEndpointDTO.getName());
        assertEquals(result.getStatus(), monitoredEndpointDTO.getStatus());
    }

    @Test
    @Order(3)
    void endpoint_edit_fail() {
        MonitoredEndpointEntity monitoredEndpointEntity = createMonitoredEndpoint();

        MonitoredEndpointDTO monitoredEndpointDTO = new MonitoredEndpointDTO();
        monitoredEndpointDTO.setName("Test endpoint edited 2");
        monitoredEndpointDTO.setUrl("https://reqres.in/api/unknown/2");

        boolean failed = false;

        try {
            monitoredEndpointService.editMonitoredEndpoint(monitoredEndpointEntity.getId(), monitoredEndpointDTO, "93f39e2f-80de-4033-99ee-249d92736a20");
        } catch (ValidationException ex){
            assertEquals(1, ex.getErrors().size());
            assertEquals(ValidationErrorConstants.NO_ENTITY_FOUND_FOR_ACCESS_TOKEN, ex.getErrors().get(0).getErrorCode());
            failed = true;
        }
        assertTrue(failed);
    }

    @Test
    @Order(4)
    void endpoint_remove_success() {
        MonitoredEndpointEntity monitoredEndpointEntity = createMonitoredEndpoint();
        monitoringResultService.startTask(monitoredEndpointEntity);
        taskCount = monitoredEndpointService.getMonitoringResultService().getTasks().size();
        monitoredEndpointService.deleteMonitoredEndpoint(monitoredEndpointEntity.getId(), UserIntegrationTest.ACCESS_TOKEN);

        assertEquals(taskCount - 1, monitoredEndpointService.getMonitoringResultService().getTasks().size());
        assertEquals(0, monitoredEndpointService.getMonitoredEndpointsForAccessToken(UserIntegrationTest.ACCESS_TOKEN).size());
    }

    private UserEntity createUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setAccessToken(UserIntegrationTest.ACCESS_TOKEN);
        userEntity.setUsername("Unit test");
        userEntity.setEmail("test@example.com");

        return userRepository.saveAndFlush(userEntity);
    }

    private MonitoredEndpointEntity createMonitoredEndpoint(){
        MonitoredEndpointEntity monitoredEndpointEntity = new MonitoredEndpointEntity();
        monitoredEndpointEntity.setName("Test endpoint edited");
        monitoredEndpointEntity.setUrl("http://worldtimeapi.org/api/timezone/Europe/Prague");
        monitoredEndpointEntity.setMonitoredInterval(50 * 1000);
        monitoredEndpointEntity.setOwner(createUser());

        return monitoredEndpointRepository.saveAndFlush(monitoredEndpointEntity);
    }
}
