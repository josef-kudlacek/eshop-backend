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

    @Value("${jwt.customer.expiration}")
    @DurationUnit(ChronoUnit.DAYS)
    private Duration jwtCustomerExpiration;

    @Value("${jwt.user.expiration}")
    @DurationUnit(ChronoUnit.HOURS)
    private Duration jwtUserExpiration;

    @Value("${jwt.payment.expiration}")
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration jwtPaymentExpiration;

    public long getJwtCustomerExpiration() {
        return jwtCustomerExpiration.toMillis();
    }

    public long getJwtUserExpiration() {
        return jwtUserExpiration.toMillis();
    }

    public long getJwtPaymentExpiration() {
        return jwtPaymentExpiration.toMillis();
    }
}
