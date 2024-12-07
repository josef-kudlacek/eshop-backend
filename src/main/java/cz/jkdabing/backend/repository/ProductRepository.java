package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
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

    @Query("""
            SELECT productEntity
            FROM ProductEntity productEntity
            WHERE productEntity.isActive = true
                AND (
                        productEntity.publishedDate IS NOT NULL
                        AND productEntity.publishedDate <= :currentDate
                )
                AND (
                        productEntity.withdrawalDate IS NULL
                        OR productEntity.withdrawalDate > :currentDate
                )
            """)
    List<ProductEntity> findActiveProducts(@Param("currentDate") ZonedDateTime currentDate);
}
