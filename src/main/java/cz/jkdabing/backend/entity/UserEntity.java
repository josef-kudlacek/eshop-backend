package cz.jkdabing.backend.entity;

import cz.jkdabing.backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<String> roles = new ArrayList<>();

    @Column(length = 68, nullable = false)
    private String password;

    private boolean isActive = false;

    private String activationToken;

    private int tokenVersion = 0;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CustomerEntity customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getUsername() {
        return this.customer.getCustomerId().toString();
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @PrePersist
    protected void onCreate() {
        this.setRoles(new ArrayList<>());
        this.roles.add(UserRole.ROLE_USER.toString());
    }
}
