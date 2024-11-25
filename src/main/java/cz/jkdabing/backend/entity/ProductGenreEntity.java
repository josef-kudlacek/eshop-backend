package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.ProductGenreType;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    //TODO: Rename to ProductGenreType
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductGenreType genreType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
