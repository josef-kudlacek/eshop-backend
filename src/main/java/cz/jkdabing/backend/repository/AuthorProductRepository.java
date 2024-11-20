package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.AuthorProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorProductRepository extends JpaRepository<AuthorProductEntity, UUID> {
}
