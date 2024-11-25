package cz.jkdabing.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorDTO {

    private UUID authorId;

    private String firstName;

    @NotBlank(message = "{error.author.last.name.empty}")
    private String lastName;
}
