package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.ProductFormatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFormatRepository extends JpaRepository<ProductFormatEntity, Integer> {
}
