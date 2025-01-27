package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.validation.annotation.ValidAddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {

    @NotBlank(message = "{error.customer.street.empty}")
    private String street;

    @NotBlank(message = "{error.customer.city.empty}")
    private String city;

    @NotBlank(message = "{error.customer.postal.code.empty}")
    private String postalCode;

    @NotBlank(message = "{error.customer.country.empty}")
    private String country;

    @ValidAddressType
    private String addressType;
}
