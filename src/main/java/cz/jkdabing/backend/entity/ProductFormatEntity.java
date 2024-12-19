package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.entity.key.ProductFormatKey;
import cz.jkdabing.backend.enums.AudioFormatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_format")
public class ProductFormatEntity {

    @EmbeddedId
    private ProductFormatKey id;

    @Enumerated(EnumType.STRING)
    private AudioFormatType audioFormatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
