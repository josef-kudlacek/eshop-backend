package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductGenreDTO {

    @NotNull(message = "{error.product.id.empty}")
    private UUID productId;

    @NotEmpty(message = "{error.product.genre.type.empty}")
    private String genreType;
}
