package io.dxheroes.endpointsmonitoringservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity(name = "MonitoringResult")
public class MonitoringResultEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime dateOfCheck;

    @Column(nullable = false)
    private Integer httpStatusCode;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @ManyToOne
    private MonitoredEndpointEntity monitoredEndpoint;

}
