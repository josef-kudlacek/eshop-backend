package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorDTO {

    private UUID authorId;

    @NotBlank(message = "{error.author.first.name.empty}")
    private String firstName;

    @NotBlank(message = "{error.author.last.name.empty}")
    private String lastName;
}
