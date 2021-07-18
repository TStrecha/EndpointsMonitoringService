package io.dxheroes.endpointsmonitoringservice.controller.v1;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.dto.UserDTO;
import io.dxheroes.endpointsmonitoringservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{accessToken}")
    @ApiOperation(value = "Get user by access token", notes = "Returns more information about user based on their access token.", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserByAccessToken(@PathVariable(name = "accessToken") String accessToken){
        Utils.validateAccessToken(accessToken);

        return userService.getUserByAccessToken(accessToken);
    }

    @PostMapping
    @ApiOperation(value = "Create user", notes = "Creates user from DTO. When userDTO.accessToken is null, creates a random UUID as accessToken.", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        Utils.validateAccessToken(userDTO.getAccessToken(), userDTO.getClass());

        return userService.createUser(userDTO);
    }

    @PutMapping
    @ApiOperation(value = "Edits user", notes = "Edits a user based on access token. The access token in userDTO and in the header have to match.", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO editUser(@RequestBody UserDTO userDTO, @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);
        Utils.validateAccessToken(userDTO.getAccessToken(), userDTO.getClass());

        return userService.editUser(userDTO, accessToken);
    }

}
