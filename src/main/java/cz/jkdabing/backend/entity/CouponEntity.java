package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @ManyToMany
    @JoinTable(
            name = "product_coupon",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private List<ProductEntity> applicableProducts;

    private boolean isActive;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private LocalDate expirationDate;

    private Integer maxUsageCount;

    private Integer usageCount;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
