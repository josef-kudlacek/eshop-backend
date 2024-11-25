package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.ProductGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGenreRepository extends JpaRepository<ProductGenreEntity, Long> {
}
