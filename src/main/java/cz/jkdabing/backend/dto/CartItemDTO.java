package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.dto.response.ImageResponse;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private UUID cartId;

    private UUID cartItemId;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal cartItemPrice;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal cartItemVat;

    @NotNull
    private Integer quantity;

    @NotNull
    private UUID productId;

    private String productName;

    private String productType;

    private ImageResponse image;
}
