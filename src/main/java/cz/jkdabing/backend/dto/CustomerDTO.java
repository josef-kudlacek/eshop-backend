package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerDTO {

    private UUID customerId;

    @NotBlank(message = "Email may not be empty")
    private String email;

    @NotBlank(message = "First name may not be empty")
    private String firstName;

    @NotBlank(message = "Last name may not be empty")
    private String lastName;

    private String companyName;

    private String phoneNumber;

    @NotBlank(message = "Street may not be empty")
    private String street;

    @NotBlank(message = "City may not be empty")
    private String city;

    @NotBlank(message = "Postal code may not be empty")
    private String postalCode;

    @NotBlank(message = "Country may not be empty")
    private String country;
}
