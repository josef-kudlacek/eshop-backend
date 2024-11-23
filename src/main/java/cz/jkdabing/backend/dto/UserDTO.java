package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.validation.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID userId;

    @NotBlank(message = "Username may not be empty")
    private String username;

    @ValidPassword
    private String password;

    @Valid
    private CustomerDTO customer;
}
