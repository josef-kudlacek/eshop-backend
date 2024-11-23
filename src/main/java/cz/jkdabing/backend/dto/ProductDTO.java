package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductDTO {

    private UUID productId;

    @NotBlank(message = "{error.product.name}")
    private String productName;

    @NotBlank(message = "{error.product.type}")
    private String productType;

    @NotBlank(message = "{error.product.description}")
    private String productDescription;

    private ZonedDateTime publishedDate;

    private ZonedDateTime withdrawalDate;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal vat;
}
