package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorDTO {

    private UUID authorId;

    @NotBlank(message = "First name may not be empty")
    private String firstName;

    @NotBlank(message = "Last name may not be empty")
    private String lastName;
}
