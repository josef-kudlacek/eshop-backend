package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<AuthorProductEntity> authorProductSet = new HashSet<>();

    @Column(nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @OneToMany(mappedBy = "product")
    private Set<ProductGenreEntity> genres = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @OneToMany(mappedBy = "product")
    private Set<ProductFormatEntity> formats = new HashSet<>();

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal vat;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime publishedDate;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime withdrawalDate;

    @OneToOne
    @JoinColumn(name = "example_audio")
    private AudioFileEntity example;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioFileEntity> audioFiles = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();

    @ManyToMany(mappedBy = "applicableProducts")
    private List<CouponEntity> coupons = new ArrayList<>();

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
