package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.validation.annotation.ValidAuthorType;
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

    @ValidAuthorType
    private String authorType;
}
