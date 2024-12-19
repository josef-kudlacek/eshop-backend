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
@Table(name = "audit_logs")
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID auditLogId;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime loggedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String tableName;

    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String action;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }
}
