package cz.jkdabing.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
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

    @Valid
    @NotEmpty(message = "{error.customer.address.empty}")
    private List<AddressDTO> addresses;
}
