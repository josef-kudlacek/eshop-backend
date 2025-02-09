package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.security.config.SecurityConfig;
import cz.jkdabing.backend.util.ConversionClassUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderImpl.class);

    private final SecurityConfig securityConfig;

    public JwtTokenProviderImpl(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    public String createCustomerToken(String customerId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + securityConfig.getJwtCustomerAccessTokenExpiration());

        return Jwts.builder()
                .subject(customerId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(ConversionClassUtil.convertStringToSecretKey(securityConfig.getSecretKey()))
                .compact();
    }

    @Override
    public String createUserToken(int tokenVersion, String customerId, String userId, String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + securityConfig.getJwtUserAccessTokenExpiration());

        return Jwts.builder()
                .subject(customerId)
                .claim(JWTConstants.TOKEN_VERSION, tokenVersion)
                .claim(JWTConstants.USER_ID, userId)
                .claim(JWTConstants.ROLES, roles)
                .issuedAt(now)
                .expiration(validity)
                .signWith(ConversionClassUtil.convertStringToSecretKey(securityConfig.getSecretKey()))
                .compact();
    }

    @Override
    public String getSubjectIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(ConversionClassUtil.convertStringToSecretKey(securityConfig.getSecretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(ConversionClassUtil.convertStringToSecretKey(securityConfig.getSecretKey()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getUserClaimsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims.get(JWTConstants.ROLES) != null) {
            return Map.of(
                    JWTConstants.ROLES, claims.get(JWTConstants.ROLES),
                    JWTConstants.TOKEN_VERSION, claims.get(JWTConstants.TOKEN_VERSION),
                    JWTConstants.USER_ID, claims.get(JWTConstants.USER_ID)
            );
        }

        return Collections.emptyMap();
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .verifyWith(ConversionClassUtil.convertStringToSecretKey(securityConfig.getSecretKey()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception exception) {
            logger.error("Failed to parse claims from token: {}", token, exception);
        }

        return claims;
    }
}
