package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "coupons")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID couponId;

    @Size(max = 50)
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @ManyToMany
    @JoinTable(
            name = "coupon_product",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> applicableProducts;

    private boolean isActive = false;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime expirationDate;

    private String email;

    private Integer maxUsageCount;

    private Integer usageCount;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    @NotNull
    private UserEntity createdBy;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;
}
