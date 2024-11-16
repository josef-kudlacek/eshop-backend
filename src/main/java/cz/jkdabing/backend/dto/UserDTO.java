package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID userId;

    @NotBlank(message = "Email may not be empty")
    private String email;

    @NotBlank(message = "User name may not be empty")
    private String userName;

    @NotBlank(message = "Password may not be empty")
    private String password;

    private CustomerDTO customer;
}
