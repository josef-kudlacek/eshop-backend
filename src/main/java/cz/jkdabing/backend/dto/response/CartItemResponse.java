package cz.jkdabing.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartItemResponse {

    private UUID cartItemId;

    private UUID productId;

    private String productName;

    private String productType;

    private ImageResponse image;

    private Integer quantity;

    private BigDecimal cartItemPrice;

    private BigDecimal cartItemVat;

    private CouponResponse coupon;

}
