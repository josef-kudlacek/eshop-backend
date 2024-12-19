package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @NotNull
    private OrderEntity order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal orderItemPrice;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal orderItemVat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @OneToMany(mappedBy = "orderItem")
    private List<UserAccessEntity> userAccessList = new ArrayList<>();

    public void applyCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public void removeCoupon() {
        this.coupon = null;
    }
}
