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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductAuthorEntity> productAuthorSet = new HashSet<>();

    @Column(nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductGenreEntity> genres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductFormatEntity> formats = new HashSet<>();

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal vat;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime publishedDate;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime withdrawalDate;

    private boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "example_audio")
    private AudioFileEntity example;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioFileEntity> audioFiles = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();

    @ManyToMany(mappedBy = "applicableProducts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CouponEntity> coupons = new ArrayList<>();

    /**
     * Copies the product entity and all its related entities to the new cloned product entity.
     *
     * @return the cloned product entity
     */
    public ProductEntity copy() {
        ProductEntity productEntityClone = ProductEntity.builder()
                .productId(null)
                .productName(this.productName)
                .productType(this.productType)
                .image(this.image)
                .productDescription(this.productDescription)
                .price(this.price)
                .vat(this.vat)
                .publishedDate(this.publishedDate)
                .withdrawalDate(this.withdrawalDate)
                .isActive(this.isActive)
                .example(this.example)
                .productAuthorSet(new HashSet<>())
                .genres(new HashSet<>())
                .formats(new HashSet<>())
                .audioFiles(new ArrayList<>())
                .files(new ArrayList<>())
                .coupons(new ArrayList<>())
                .build();

        Optional.ofNullable(this.productAuthorSet)
                .ifPresent(productAuthorEntities ->
                        productAuthorEntities.forEach(productAuthorEntity -> {
                                    ProductAuthorEntity productAuthorEntityClone = ProductAuthorEntity.builder()
                                            .product(productEntityClone)
                                            .author(productAuthorEntity.getAuthor())
                                            .authorType(productAuthorEntity.getAuthorType())
                                            .build();

                                    productEntityClone.getProductAuthorSet().add(productAuthorEntityClone);
                                }
                        )
                )
        ;

        Optional.ofNullable(this.genres)
                .ifPresent(productGenreEntities ->
                        productGenreEntities.forEach(productGenreEntity -> {
                            ProductGenreEntity productGenreEntityClone = ProductGenreEntity.builder()
                                    .product(productEntityClone)
                                    .genreType(productGenreEntity.getGenreType())
                                    .build();

                            productEntityClone.getGenres().add(productGenreEntityClone);
                        })
                )
        ;

        Optional.ofNullable(this.formats)
                .ifPresent(productFormatEntities ->
                        productFormatEntities.forEach(productFormatEntity -> {
                            ProductFormatEntity productFormatEntityClone = ProductFormatEntity.builder()
                                    .product(productEntityClone)
                                    .formatType(productFormatEntity.getFormatType())
                                    .build();

                            productEntityClone.getFormats().add(productFormatEntityClone);
                        })
                )
        ;

        Optional.ofNullable(this.audioFiles)
                .ifPresent(audioFileEntities ->
                        audioFileEntities.forEach(audioFileEntity -> {
                                    AudioFileEntity audioFileEntityClone = AudioFileEntity.builder()
                                            .product(productEntityClone)
                                            .audioFormatType(audioFileEntity.getAudioFormatType())
                                            .length(audioFileEntity.getLength())
                                            .bitrate(audioFileEntity.getBitrate())
                                            .trackNumber(audioFileEntity.getTrackNumber())
                                            .isSample(audioFileEntity.isSample())
                                            .fileName(audioFileEntity.getFileName())
                                            .fileUrl(audioFileEntity.getFileUrl())
                                            .size(audioFileEntity.getSize())
                                            .uploadDate(audioFileEntity.getUploadDate())
                                            .build();

                                    productEntityClone.getAudioFiles().add(audioFileEntityClone);
                                }
                        )
                )
        ;

        Optional.ofNullable(this.files)
                .ifPresent(fileEntities ->
                        fileEntities.forEach(fileEntity -> {
                                    FileEntity fileEntityClone = FileEntity.builder()
                                            .product(productEntityClone)
                                            .fileName(fileEntity.getFileName())
                                            .fileUrl(fileEntity.getFileUrl())
                                            .size(fileEntity.getSize())
                                            .uploadDate(fileEntity.getUploadDate())
                                            .accessEntityList(new ArrayList<>(fileEntity.getAccessEntityList()))
                                            .build();

                                    productEntityClone.getFiles().add(fileEntityClone);
                                }
                        )
                )
        ;

        Optional.ofNullable(this.coupons)
                .ifPresent(couponEntities ->
                        couponEntities.forEach(couponEntity -> {
                                    couponEntity.getApplicableProducts().add(productEntityClone);
                                    productEntityClone.getCoupons().add(couponEntity);
                                }
                        )
                )
        ;

        return productEntityClone;
    }
}
