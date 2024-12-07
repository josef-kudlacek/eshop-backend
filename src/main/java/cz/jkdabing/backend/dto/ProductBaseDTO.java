package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.dto.response.ImageResponse;
import cz.jkdabing.backend.dto.response.ProductAuthorResponse;
import cz.jkdabing.backend.util.PriceUtils;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBaseDTO {

    private UUID productId;

    @NotBlank(message = "{error.product.name}")
    private String productName;

    @NotBlank(message = "{error.product.type}")
    private String productType;

    private ImageResponse image;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal vat;

    @DecimalMin(value = "0.0")
    private BigDecimal totalPrice;

    private List<ProductAuthorResponse> authors;

    public BigDecimal getTotalPrice() {
        return PriceUtils.calculateTotalPrice(this.price, this.vat);
    }
}
