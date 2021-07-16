package io.dxheroes.endpointsmonitoringservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MonitoringResultDTO {

    @ApiModelProperty(hidden = true)
    private Long id;
    private OffsetDateTime dateOfCheck;
    private Integer httpStatusCode;
    private String payload;
    private MonitoredEndpointDTO monitoredEndpoint;

}
