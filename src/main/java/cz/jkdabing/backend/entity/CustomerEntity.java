package cz.jkdabing.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String email;

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phoneNumber;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<OrderEntity> orders = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
