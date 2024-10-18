package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.entity.key.ProductGenreKey;
import cz.jkdabing.backend.enums.GenreType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_genre")
public class ProductGenreEntity {

    @EmbeddedId
    private ProductGenreKey id;

    @Enumerated(EnumType.STRING)
    private GenreType genreType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
