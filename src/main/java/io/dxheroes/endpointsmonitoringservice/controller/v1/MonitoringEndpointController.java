package io.dxheroes.endpointsmonitoringservice.controller.v1;

import io.dxheroes.endpointsmonitoringservice.Utils;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoredEndpointDTO;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoringResultDTO;
import io.dxheroes.endpointsmonitoringservice.dto.StatusDTO;
import io.dxheroes.endpointsmonitoringservice.service.MonitoredEndpointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/monitored-endpoint")
public class MonitoringEndpointController {

    @Autowired
    private MonitoredEndpointService monitoredEndpointService;

    @PostMapping
    @ApiOperation(value = "Create monitored endpoint", notes = "Creates monitored endpoint linked with user according to the  access token.", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoredEndpointDTO createMonitoredEndpoint(@RequestBody MonitoredEndpointDTO monitoredEndpointDTO, @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoredEndpointService.createMonitoredEndpoint(monitoredEndpointDTO, accessToken);
    }

    @DeleteMapping("/{monitored-endpoint-id}")
    @ApiOperation(value = "Delete monitored endpoint", notes = "Deletes monitored endpoint that is linked with user according to the  access token.", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusDTO deleteMonitoredEndpoint(@PathVariable("monitored-endpoint-id") Long monitoredEndpointId, @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoredEndpointService.deleteMonitoredEndpoint(monitoredEndpointId, accessToken);
    }

    @GetMapping
    @ApiOperation(value = "Get monitored endpoints", notes = "Gets all monitored endpoints that are linked with user according to the  access token.", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoredEndpointDTO> getMonitoredEndpointsForAccessToken(@RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoredEndpointService.getMonitoredEndpointsForAccessToken(accessToken);
    }

    @PutMapping("/{monitored-endpoint-id}")
    @ApiOperation(value = "Edit monitored endpoint", notes = "Edits monitored endpoints that is linked with user according to the access token.", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoredEndpointDTO editMonitoredEndpoint(@PathVariable("monitored-endpoint-id") Long monitoredEndpointId, @RequestBody MonitoredEndpointDTO monitoredEndpointDTO, @RequestHeader("access-token") String accessToken){
        Utils.validateAccessToken(accessToken);

        return monitoredEndpointService.editMonitoredEndpoint(monitoredEndpointId, monitoredEndpointDTO, accessToken);
    }

}
