package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.validation.annotation.ValidProductFormatType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductFormatDTO {

    @NotNull(message = "{error.product.id.empty}")
    private UUID productId;

    @ValidProductFormatType
    private String productFormatType;
}
