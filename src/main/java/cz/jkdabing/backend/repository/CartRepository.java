package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.CartEntity;
import cz.jkdabing.backend.enums.CartStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    @Query("""
            SELECT c FROM CartEntity c
            WHERE c.cartId = :id
            AND c.statusType IN :statusTypes
            AND c.customer.customerId = :customerId
            """)
    Optional<CartEntity> findByCartIdAndStatusTypeAndCustomer_CustomerId(UUID id, List<CartStatusType> statusTypes, UUID customerId);

    @Query("""
            SELECT c FROM CartEntity c
            WHERE c.statusType IN :statusTypes
            AND c.customer.customerId = :customerId
            """)
    Optional<CartEntity> findByStatusTypeInAndCustomer_CustomerId(List<CartStatusType> statusTypes, UUID customerId);
}
