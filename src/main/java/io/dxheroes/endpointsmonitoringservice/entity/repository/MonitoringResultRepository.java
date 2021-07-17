package io.dxheroes.endpointsmonitoringservice.entity.repository;

import io.dxheroes.endpointsmonitoringservice.entity.MonitoringResultEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface MonitoringResultRepository extends PagingAndSortingRepository<MonitoringResultEntity, Long> {

    @Query("SELECT mr FROM MonitoringResult mr WHERE mr.monitoredEndpoint.id = :monitoredEndpoint ORDER BY mr.dateOfCheck DESC")
    List<MonitoringResultEntity> getAllForMonitoredEndpointLimited(Long monitoredEndpoint, Pageable pageable);

    List<MonitoringResultEntity> getByDateOfCheckAfterAndDateOfCheckBefore(OffsetDateTime from, OffsetDateTime to);
}
