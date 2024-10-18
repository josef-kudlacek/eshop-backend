package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(length = 68, nullable = false)
    private String password;

    private boolean isActive = false;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

}
