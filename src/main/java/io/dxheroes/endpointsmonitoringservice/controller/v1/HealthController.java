package io.dxheroes.endpointsmonitoringservice.controller.v1;

import io.dxheroes.endpointsmonitoringservice.dto.StatusDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health-check")
public class HealthController {

	@GetMapping
	@ApiOperation(value = "Health check", notes = "Used to verify app's status, also returns a timestamp.", produces = MediaType.APPLICATION_JSON_VALUE)
	public StatusDTO healthCheck(){
		return new StatusDTO(StatusDTO.Status.SUCCESS);
	}
}
