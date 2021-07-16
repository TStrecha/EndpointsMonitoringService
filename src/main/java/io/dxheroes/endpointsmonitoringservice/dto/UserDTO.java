package io.dxheroes.endpointsmonitoringservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDTO {

    @ApiModelProperty(hidden = true)
    private Long id;
    private String username;
    private String email;
    private String accessToken; //UUID
}
