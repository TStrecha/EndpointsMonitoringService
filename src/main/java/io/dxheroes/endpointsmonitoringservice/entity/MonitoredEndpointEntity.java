package io.dxheroes.endpointsmonitoringservice.entity;

import io.dxheroes.endpointsmonitoringservice.constant.Status;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "MonitoredEndpoint")
public class MonitoredEndpointEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime dateOfCreation;

    private OffsetDateTime dateOfLastCheck;

    @Column(nullable = false)
    private Integer monitoredInterval;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    private UserEntity owner;

    @OneToMany(mappedBy = "monitoredEndpoint", cascade = CascadeType.ALL)
    private List<MonitoringResultEntity> monitoringResults = new ArrayList<>();
}
