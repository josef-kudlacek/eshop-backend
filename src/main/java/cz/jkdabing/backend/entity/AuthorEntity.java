package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID authorId;

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<ProductAuthorEntity> authorProductSet = new HashSet<>();

}
