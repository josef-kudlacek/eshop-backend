package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.ProductFormatType;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer formatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductFormatType formatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
