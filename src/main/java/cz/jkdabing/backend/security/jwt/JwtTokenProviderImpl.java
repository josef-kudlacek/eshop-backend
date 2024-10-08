package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.utils.ConversionClassUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final long DAY_VALIDITY_IN_MILLISECONDS = 86400000;

    @Value("${spring.security.jwt.key}")
    private String key;

    @Override
    public String createToken(String customerId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + DAY_VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .subject(customerId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(ConversionClassUtil.convertStringToSecretKey(key))
                .compact();
    }

    @Override
    public String getCustomerIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(ConversionClassUtil.convertStringToSecretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(ConversionClassUtil.convertStringToSecretKey(key))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
