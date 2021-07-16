package io.dxheroes.endpointsmonitoringservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(length = 36, nullable = false)
    private String accessToken; //UUID

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    List<MonitoredEndpointEntity> monitoredEndpoints = new ArrayList<>();

}
