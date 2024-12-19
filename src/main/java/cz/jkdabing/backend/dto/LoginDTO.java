package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {

    @NotBlank(message = "{error.user.username.empty}")
    private String username;

    @NotBlank(message = "{error.customer.password.empty}")
    private String password;
}
