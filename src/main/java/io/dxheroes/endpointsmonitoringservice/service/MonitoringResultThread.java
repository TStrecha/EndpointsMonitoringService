package io.dxheroes.endpointsmonitoringservice.service;

import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoringResultEntity;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoredEndpointRepository;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoringResultRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Data
@PersistenceContext
public class MonitoringResultThread implements Runnable{

    private final MonitoredEndpointEntity monitoredEndpointEntity;
    private final RestTemplate restTemplate;
    private final MonitoredEndpointRepository monitoredEndpointRepository;
    private final MonitoringResultRepository monitoringResultRepository;

    @Override
    @Transactional
    public void run() {
        MonitoringResultEntity resultEntity = new MonitoringResultEntity();
        resultEntity.setDateOfCheck(OffsetDateTime.now());
        monitoredEndpointEntity.setDateOfLastCheck(OffsetDateTime.now());

        ResponseEntity<String> response = this.restTemplate.getForEntity(monitoredEndpointEntity.getUrl(), String.class);

        resultEntity.setMonitoredEndpoint(monitoredEndpointEntity);
        resultEntity.setHttpStatusCode(response.getStatusCodeValue());
        resultEntity.setPayload(response.getBody());

  //      monitoredEndpointRepository.save(monitoredEndpointEntity);
//        monitoringResultRepository.save(resultEntity);
    }
}
