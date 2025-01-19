package cz.jkdabing.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jkdabing.backend.validation.annotation.ValidDiscount;
import cz.jkdabing.backend.validation.annotation.ValidDiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidDiscount
public class CouponDTO {

    private UUID couponId;

    @NotBlank(message = "{error.coupon.code.empty}")
    private String code;

    private String description;

    @ValidDiscountType
    private String discountType;

    @NotNull
    private BigDecimal discountValue;

    private LocalDate expirationDate;

    @NotNull
    @JsonProperty("isActive")
    private boolean isActive;

    private Integer maxUsageCount;

    private List<ProductBasicDTO> applicableProducts;
}
