package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @NotNull
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal cartItemPrice;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal cartItemVat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    public void applyCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public void removeCoupon() {
        this.coupon = null;
    }
}