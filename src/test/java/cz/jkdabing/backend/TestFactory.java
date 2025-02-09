package cz.jkdabing.backend;

import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.constants.ApiPathConstants;
import cz.jkdabing.backend.dto.AddressDTO;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.AddressEntity;
import cz.jkdabing.backend.entity.CustomerEntity;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;

public class TestFactory {

    private TestFactory() {
    }

    public static Cookie prepareCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(ApiPathConstants.API_PATH);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(true);
        return cookie;
    }

    public static CustomerDTO prepareCustomerDTO() {
        ArrayList<AddressDTO> addresses = new ArrayList<>();
        addresses.add(prepareAddressDTO());

        return CustomerDTO.builder()
                .firstName("Jmeno")
                .lastName("Prijmeni")
                .email("jmeno.prijmeni@email.com")
                .addresses(addresses)
                .build();
    }

    public static CustomerEntity prepareCustomerEntity() {
        ArrayList<AddressEntity> addresses = new ArrayList<>();
        addresses.add(prepareAddressEntity());

        return CustomerEntity.builder()
                .customerId(CustomerTestConstants.CUSTOMER_ID_UUID)
                .firstName("Jmeno")
                .lastName("Prijmeni")
                .email("jmeno.prijmeni@email.com")
                .addresses(addresses)
                .build();
    }

    public static AddressEntity prepareAddressEntity() {
        return AddressEntity.builder()
                .street("Ulice 1")
                .city("Mesto")
                .postalCode("100 00")
                .country("Česká republika")
                .build();
    }

    public static AddressDTO prepareAddressDTO() {
        return AddressDTO.builder()
                .street("Ulice 1")
                .city("Mesto")
                .postalCode("100 00")
                .country("Česká republika")
                .addressType("BILLING")
                .build();
    }
}
