package io.dxheroes.endpointsmonitoringservice.integration;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.UserDTO;
import io.dxheroes.endpointsmonitoringservice.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilsTest {

    @Test
    @Order(1)
    void uuid() {
        assertTrue(Utils.isValidUUID("93f39e2f-80de-4033-99ee-249d92736a25"));
        assertTrue(Utils.isValidUUID("dcb20f8a-5657-4f1b-9f7f-ce65739b359e"));
        assertFalse(Utils.isValidUUID("93f39e2f-80de-4033-99ee-249d92736g25"));
        assertFalse(Utils.isValidUUID("93f39e2f-80de--99ee-249d92736g25"));
        assertFalse(Utils.isValidUUID("93f39e2f-80de-4033-99ee-249d92736a25f"));
    }

    @Test
    @Order(2)
    void email() {
        assertTrue(Utils.isValidEmail("example@gmail.com"));
        assertTrue(Utils.isValidEmail("example@seznam.cz"));
        assertFalse(Utils.isValidUUID("gmail.com"));
        assertFalse(Utils.isValidUUID("@gmail.com"));
        assertFalse(Utils.isValidUUID("example@gmail"));
    }
    @Test
    @Order(3)
    void url() {
        assertTrue(Utils.isValidURL("http://www.test.com"));
        assertTrue(Utils.isValidURL("https://www.example.co.uk"));
        assertFalse(Utils.isValidURL("www.example.com"));
        assertFalse(Utils.isValidURL("https://example"));
        assertFalse(Utils.isValidURL("ftp://www.example.com"));
    }

}
