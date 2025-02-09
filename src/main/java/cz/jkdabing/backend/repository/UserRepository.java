package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByActivationToken(String token);

    @EntityGraph(attributePaths = {"roles", "customer"})
    @Transactional(readOnly = true)
    Optional<UserEntity> findById(UUID userId);
}
