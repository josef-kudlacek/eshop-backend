package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CustomerDTO;

public interface CustomerService {

    Long registerCustomer(CustomerDTO customerDTO);
}
