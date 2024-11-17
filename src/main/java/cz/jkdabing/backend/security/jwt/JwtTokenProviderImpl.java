package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.utils.ConversionClassUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Value("${spring.security.jwt.key}")
    private String secretKey;

    @Value("${jwt.customer.expiration}")
    private long jwtCustomerExpiration;

    @Value("${jwt.user.expiration}")
    private long jwtUserExpiration;

    @Override
    public String createCustomerToken(String customerId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtCustomerExpiration);

        return Jwts.builder()
                .subject(customerId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(ConversionClassUtil.convertStringToSecretKey(secretKey))
                .compact();
    }

    @Override
    public String createUserToken(UserDetails userDetails) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtUserExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(now)
                .expiration(validity)
                .signWith(ConversionClassUtil.convertStringToSecretKey(secretKey))
                .compact();
    }

    @Override
    public String getCustomerIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(ConversionClassUtil.convertStringToSecretKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(ConversionClassUtil.convertStringToSecretKey(secretKey))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
