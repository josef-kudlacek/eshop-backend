package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.mapper.CustomerMapper;
import cz.jkdabing.backend.repository.CustomerRepository;
import cz.jkdabing.backend.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public UUID createCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerMapper.toEntity(customerDTO);
        customerEntity.setCreatedAt(ZonedDateTime.now());
        customerEntity.setUpdatedAt(ZonedDateTime.now());
        customerRepository.saveAndFlush(customerEntity);

        return customerEntity.getCustomerId();
    }
}
