package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {

    @NotBlank(message = "User name may not be empty")
    private String userName;

    @NotBlank(message = "Password may not be empty")
    private String password;
}
