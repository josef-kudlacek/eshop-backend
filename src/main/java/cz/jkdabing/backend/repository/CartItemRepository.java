package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

    Optional<CartItemEntity> findByCartItemIdAndCart_Customer_CustomerId(UUID cartItemId, UUID customerId);
}
