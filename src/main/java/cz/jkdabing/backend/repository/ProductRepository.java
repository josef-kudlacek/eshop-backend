package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("""
            SELECT productEntity
            FROM ProductEntity productEntity
            LEFT JOIN FETCH productEntity.productAuthorSet productAuthorSet
            LEFT JOIN FETCH productAuthorSet.author
            WHERE productEntity.productId = :productId
            """)
    Optional<ProductEntity> findProductDetailByProductId(@Param("productId") UUID productId);
}
