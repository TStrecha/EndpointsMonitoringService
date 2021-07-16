package io.dxheroes.endpointsmonitoringservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MonitoredEndpointDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;
    private String url;
    private OffsetDateTime dateOfCreation;
    private OffsetDateTime dateOfLastCheck;
    private Integer monitoredIntervalInMillis;

    @ApiModelProperty(hidden = true)
    private UserDTO owner;

}
