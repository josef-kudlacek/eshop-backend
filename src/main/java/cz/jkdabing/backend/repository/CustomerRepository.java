package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    //TODO: Remove this method
    Optional<CustomerEntity> findByUser_userId(UUID userId);

    Optional<CustomerEntity> findCustomerByUser_username(String username);
}
