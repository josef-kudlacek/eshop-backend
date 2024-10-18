package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private ImageEntity imageEntity;

    private String productDescription;

    @OneToMany(mappedBy = "product")
    private Set<ProductFormatEntity> formats = new HashSet<>();

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal price;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime publishedDate;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime withdrawalDate;
}
