package io.dxheroes.endpointsmonitoringservice.entity.repository;

import io.dxheroes.endpointsmonitoringservice.entity.MonitoredEndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpointEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT me FROM MonitoredEndpoint me WHERE me.owner.accessToken = :accessToken AND me.id = :id")
    MonitoredEndpointEntity getForAccessToken(Long id, String accessToken);

    @Query("SELECT me FROM MonitoredEndpoint me WHERE me.owner.accessToken = :accessToken")
    List<MonitoredEndpointEntity> getAllForAccessToken(String accessToken);
}
