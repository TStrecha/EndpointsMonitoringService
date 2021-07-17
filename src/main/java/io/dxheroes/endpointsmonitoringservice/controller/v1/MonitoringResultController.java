package io.dxheroes.endpointsmonitoringservice.controller.v1;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoringResultDTO;
import io.dxheroes.endpointsmonitoringservice.service.MonitoringResultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/monitoring-result")
public class MonitoringResultController {

    @Autowired
    private MonitoringResultService monitoringResultService;

    @GetMapping("last/{count}/for/{monitored-endpoint-id}")
    @ApiOperation(value = "Get last monitoring results", notes = "Gets last monitoring results for particular monitored endpoint based on id in path limited by count.", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringResultDTO> getLastMonitoringResultsForMonitoredEndpoint(@PathVariable("count") Integer count,
                                                                                  @PathVariable("monitored-endpoint-id") Long monitoredEndpointId,
                                                                                  @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoringResultService.getLastMonitoringResultsForMonitoredEndpoint(count, monitoredEndpointId, accessToken);
    }

    @GetMapping("in-interval/{from}/{to}/for/{monitored-endpoint-id}")
    @ApiOperation(value = "Get monitoring results within a time interval", notes = "Gets last monitoring results for particular monitored endpoint within a time interval (from, to).", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringResultDTO> getMonitoringResultsWithinAnInterval(@PathVariable("monitored-endpoint-id")  Long monitoredEndpointId,
                                                                                  @PathVariable("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)OffsetDateTime from,
                                                                                  @PathVariable("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
                                                                                  @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoringResultService.getMonitoringResultsWithinAnInterval(monitoredEndpointId, from, to, accessToken);
    }


}
