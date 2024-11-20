package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorProductDTO {

    private UUID authorId;

    @NotEmpty(message = "Author type is required")
    private String authorType;
}
