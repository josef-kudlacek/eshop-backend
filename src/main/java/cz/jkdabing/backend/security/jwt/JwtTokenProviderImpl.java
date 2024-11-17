package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.utils.ConversionClassUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public String createUserToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtUserExpiration);

        return Jwts.builder()
                .subject(username)
                .claim(JWTConstants.ROLES, roles)
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
    public boolean isTokenValid(String token) {
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

    @Override
    public Map<String, Object> getUserDetailsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims.get(JWTConstants.ROLES) != null) {
            return Map.of(
                    JWTConstants.USERNAME, claims.getSubject(),
                    JWTConstants.ROLES, claims.get(JWTConstants.ROLES)
            );
        }

        return Collections.emptyMap();
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .verifyWith(ConversionClassUtil.convertStringToSecretKey(secretKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return claims;
    }
}
