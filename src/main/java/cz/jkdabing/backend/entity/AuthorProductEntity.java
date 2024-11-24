package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.AuthorType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "author_product")
public class AuthorProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID authorProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

}
