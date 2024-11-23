package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorProductDTO {

    private UUID authorId;

    @NotEmpty(message = "{error.author.type.empty}")
    private String authorType;
}
