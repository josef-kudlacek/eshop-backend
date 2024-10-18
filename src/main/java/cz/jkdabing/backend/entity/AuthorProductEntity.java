package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.entity.key.AuthorProductKey;
import cz.jkdabing.backend.enums.AuthorType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "author_product")
public class AuthorProductEntity {

    @EmbeddedId
    private AuthorProductKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

}
