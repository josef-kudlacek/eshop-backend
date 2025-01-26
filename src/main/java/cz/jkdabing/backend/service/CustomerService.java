package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.entity.UserEntity;

import java.util.UUID;

public interface CustomerService {

    void createCustomer(UserEntity userEntity, CustomerDTO customerDTO);

    void createCustomer(CustomerDTO customerDTO, UUID customerId);

    CustomerEntity getCustomerByUserNameOrThrow(String username);
}
