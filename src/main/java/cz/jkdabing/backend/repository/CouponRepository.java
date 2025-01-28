package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {

    boolean existsByCode(String couponCode);

    Optional<CouponEntity> findByCode(String couponCode);
}
