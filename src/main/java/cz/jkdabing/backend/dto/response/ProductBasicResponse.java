package cz.jkdabing.backend.dto.response;

import cz.jkdabing.backend.util.PriceUtils;
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
public class ProductBasicResponse {

    private UUID productId;

    private String productName;

    private String productType;

    private ImageResponse image;

    private BigDecimal price;

    private BigDecimal vat;

    private BigDecimal totalPrice;

    private List<ProductAuthorResponse> authors;

    public BigDecimal getTotalPrice() {
        return PriceUtils.calculateTotalPrice(this.price, this.vat);
    }
}