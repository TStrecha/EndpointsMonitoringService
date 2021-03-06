package io.dxheroes.endpointsmonitoringservice.service;

import io.dxheroes.endpointsmonitoringservice.constant.Status;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationErrorConstants;
import io.dxheroes.endpointsmonitoringservice.controller.v1.exception.ValidationException;
import io.dxheroes.endpointsmonitoringservice.dto.MonitoringResultDTO;
import io.dxheroes.endpointsmonitoringservice.dto.ValidationErrorDTO;
import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import io.dxheroes.endpointsmonitoringservice.entity.mapper.MonitoringResultMapper;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoredEndpointRepository;
import io.dxheroes.endpointsmonitoringservice.entity.repository.MonitoringResultRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@Data
public class MonitoringResultService {

    private final TaskScheduler taskScheduler;
    private final RestTemplate restTemplate;

    private final MonitoredEndpointRepository monitoredEndpointRepository;
    private final MonitoringResultRepository monitoringResultRepository;
    private final MonitoringResultMapper monitoringResultMapper;

    private final HashMap<Long, ScheduledFuture> tasks = new HashMap<>();

    @Autowired
    MonitoringResultService(TaskScheduler taskScheduler,
                            RestTemplateBuilder restTemplateBuilder, MonitoredEndpointRepository monitoredEndpointRepository,
                            MonitoringResultRepository monitoringResultRepository, MonitoringResultMapper monitoringResultMapper){
        this.taskScheduler = taskScheduler;
        this.restTemplate = restTemplateBuilder.build();
        this.monitoredEndpointRepository = monitoredEndpointRepository;
        this.monitoringResultRepository = monitoringResultRepository;
        this.monitoringResultMapper = monitoringResultMapper;

        for(MonitoredEndpointEntity monitoredEndpointEntity : monitoredEndpointRepository.getALlByStatus(Status.ACTIVE)){
            startTask(monitoredEndpointEntity);
        }
    }

    public void startTask(MonitoredEndpointEntity monitoredEndpointEntity) {
        MonitoringResultThread thread = new MonitoringResultThread(monitoredEndpointEntity, restTemplate, monitoredEndpointRepository, monitoringResultRepository);
        ScheduledFuture task = taskScheduler.scheduleAtFixedRate(thread, monitoredEndpointEntity.getMonitoredInterval());
        tasks.put(monitoredEndpointEntity.getId(), task);
    }

    public void stopTask(MonitoredEndpointEntity monitoredEndpointEntity) {
        stopTask(monitoredEndpointEntity.getId());
    }

    public void stopTask(Long monitoredEndpointId) {
        ScheduledFuture task = tasks.get(monitoredEndpointId);
        //This can happen in tests. Tests sometimes save entities into database directly, so the task doesn't start.
        if(task == null){
            return;
        }
        task.cancel(false);
        tasks.remove(monitoredEndpointId);
    }

    public List<MonitoringResultDTO> getLastMonitoringResultsForMonitoredEndpoint(Integer count, Long monitoredEndpointId, String accessToken) {
        if(count == null || count < 0){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("count")
                    .invalidValue(count)
                    .errorCode(ValidationErrorConstants.INVALID_COUNT).build();
            throw new ValidationException(validationErrorDTO);
        }

        MonitoredEndpointEntity entity = monitoredEndpointRepository.getForAccessToken(monitoredEndpointId, accessToken);

        if(entity == null){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("monitoredEndpointId")
                    .invalidValue(monitoredEndpointId)
                    .errorCode(ValidationErrorConstants.NO_ENTITY_FOUND_FOR_ACCESS_TOKEN).build();
            throw new ValidationException(validationErrorDTO);
        }

        if(count == 0){
            return new ArrayList<>();
        }

        return monitoringResultRepository.getAllForMonitoredEndpointLimited(monitoredEndpointId, PageRequest.of(0, count))
        .stream().map(monitoringResultMapper::toDTO).collect(Collectors.toList());
    }

    public List<MonitoringResultDTO> getMonitoringResultsWithinAnInterval(Long monitoredEndpointId, OffsetDateTime from, OffsetDateTime to, String accessToken) {
        MonitoredEndpointEntity entity = monitoredEndpointRepository.getForAccessToken(monitoredEndpointId, accessToken);

        if(entity == null){
            ValidationErrorDTO validationErrorDTO = ValidationErrorDTO.builder().propertyPath("monitoredEndpointId")
                    .invalidValue(monitoredEndpointId)
                    .errorCode(ValidationErrorConstants.NO_ENTITY_FOUND_FOR_ACCESS_TOKEN).build();
            throw new ValidationException(validationErrorDTO);
        }

        return monitoringResultRepository.getByDateOfCheckAfterAndDateOfCheckBefore(from, to)
                .stream().map(monitoringResultMapper::toDTO).collect(Collectors.toList());

    }
}
