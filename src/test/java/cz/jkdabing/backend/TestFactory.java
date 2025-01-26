package cz.jkdabing.backend;

import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;

public class TestFactory {

    private TestFactory() {
    }

    public static CustomerDTO prepareCustomerDTO() {
        return CustomerDTO.builder()
                .firstName("Jmeno")
                .lastName("Prijmeni")
                .email("jmeno.prijmeni@email.com")
                .street("Ulice 1")
                .city("Mesto")
                .postalCode("100 00")
                .country("Česká republika")
                .build();
    }

    public static CustomerEntity prepareCustomerEntity() {
        return CustomerEntity.builder()
                .customerId(CustomerTestConstants.CUSTOMER_ID_UUID)
                .firstName("Jmeno")
                .lastName("Prijmeni")
                .email("jmeno.prijmeni@email.com")
                .street("Ulice 1")
                .city("Mesto")
                .postalCode("100 00")
                .country("Česká republika")
                .build();
    }
}
