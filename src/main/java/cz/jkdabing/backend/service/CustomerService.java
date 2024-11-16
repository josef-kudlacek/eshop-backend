package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CustomerDTO;

import java.util.UUID;

public interface CustomerService {

    UUID createCustomer(CustomerDTO customerDTO);
}
