package io.dxheroes.endpointsmonitoringservice.dto;

import io.dxheroes.endpointsmonitoringservice.constant.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MonitoredEndpointDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;
    private String url;
    @ApiModelProperty(hidden = true)
    private OffsetDateTime dateOfCreation;
    @ApiModelProperty(hidden = true)
    private OffsetDateTime dateOfLastCheck;
    private Integer monitoredIntervalInMillis;

    private Status status;

    @ApiModelProperty(hidden = true)
    private UserDTO owner;

}
