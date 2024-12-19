package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByActivationToken(String token);

    Optional<UserEntity> findByUsername(String username);
}
