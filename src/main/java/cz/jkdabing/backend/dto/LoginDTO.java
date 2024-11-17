package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {

    @NotBlank(message = "Username may not be empty")
    private String username;

    @NotBlank(message = "Password may not be empty")
    private String password;
}
