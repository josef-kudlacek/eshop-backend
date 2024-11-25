package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductAuthorDTO {

    @NotNull(message = "{error.product.id.empty}")
    private UUID productId;

    @NotNull(message = "{error.author.id.empty}")
    private UUID authorId;

    @NotEmpty(message = "{error.author.type.empty}")
    private String authorType;
}
