package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID imageId;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String imageFormatType;

    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductEntity product;
}
