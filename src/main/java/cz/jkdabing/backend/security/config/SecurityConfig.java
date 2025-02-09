package cz.jkdabing.backend.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.jwt.key}")
    @Getter
    private String secretKey;

    @Value("${jwt.customer.access.token.expiration}")
    @DurationUnit(ChronoUnit.HOURS)
    private Duration jwtCustomerAccessTokenExpiration;

    @Value("${jwt.user.access.token.expiration}")
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration jwtUserAccessTokenExpiration;

    public long getJwtCustomerAccessTokenExpiration() {
        return jwtCustomerAccessTokenExpiration.toMillis();
    }

    public long getJwtUserAccessTokenExpiration() {
        return jwtUserAccessTokenExpiration.toMillis();
    }

    public int getUserAccessTokenExpirationSeconds() {
        return (int) jwtUserAccessTokenExpiration.toSeconds();
    }

    public int getCustomerAccessTokenExpirationSeconds() {
        return (int) jwtCustomerAccessTokenExpiration.toSeconds();
    }
}
