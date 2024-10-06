package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CustomerDTO;

public interface CustomerService {

    void registerCustomer(CustomerDTO customerDTO);
}
