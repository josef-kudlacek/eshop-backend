package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.ProductAuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductAuthorRepository extends JpaRepository<ProductAuthorEntity, UUID> {
}
