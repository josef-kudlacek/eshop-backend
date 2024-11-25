package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.validation.annotation.ValidProductGenre;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductGenreDTO {

    @NotNull(message = "{error.product.id.empty}")
    private UUID productId;

    @ValidProductGenre
    private String genreType;
}
