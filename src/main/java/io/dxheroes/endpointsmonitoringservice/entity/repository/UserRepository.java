package io.dxheroes.endpointsmonitoringservice.entity.repository;

import io.dxheroes.endpointsmonitoringservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> getByAccessToken(String accessToken);
    boolean existsByAccessToken(String accessToken);
    boolean existsByUsername(String username);

    void deleteByAccessToken(String accessToken);

}
