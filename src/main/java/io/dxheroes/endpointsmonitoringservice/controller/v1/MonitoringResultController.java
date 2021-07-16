package io.dxheroes.endpointsmonitoringservice.controller.v1;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoringResultDTO;
import io.dxheroes.endpointsmonitoringservice.service.MonitoringResultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/monitoring-result")
public class MonitoringResultController {

    @Autowired
    private MonitoringResultService monitoringResultService;

    @GetMapping("last/{count}/for/{monitored-endpoint-id}")
    @ApiOperation(value = "Get last monitoring results", notes = "Gets last monitoring results for particular monitored result based on id in path limited by count.", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringResultDTO> getLastMonitoringResultsForMonitoredEndpoint(@PathVariable("count") Integer count,
                                                                                  @PathVariable("monitored-endpoint-id") Long monitoredEndpointId,
                                                                                  @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoringResultService.getLastMonitoringResultsForMonitoredEndpoint(count, monitoredEndpointId, accessToken);
    }

}
