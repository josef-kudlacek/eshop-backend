package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerDTO {

    private UUID customerId;

    @NotBlank(message = "{error.customer.email.empty}")
    private String email;

    @NotBlank(message = "{error.customer.first.name.empty}")
    private String firstName;

    @NotBlank(message = "{error.customer.last.name.empty}")
    private String lastName;

    private String companyName;

    private String phoneNumber;

    @NotBlank(message = "{error.customer.street.empty}")
    private String street;

    @NotBlank(message = "{error.customer.city.empty}")
    private String city;

    @NotBlank(message = "{error.customer.postal.code.empty}")
    private String postalCode;

    @NotBlank(message = "{error.customer.country.empty}")
    private String country;
}
