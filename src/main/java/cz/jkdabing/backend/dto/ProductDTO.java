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

    @NotBlank(message = "Product name may not be empty")
    private String productName;

    @NotBlank(message = "Product type may not be empty")
    private String productType;

    @NotBlank(message = "Product description may not be empty")
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
