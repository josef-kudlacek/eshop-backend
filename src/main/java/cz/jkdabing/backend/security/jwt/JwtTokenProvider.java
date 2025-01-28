package cz.jkdabing.backend.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

public interface JwtTokenProvider {

    String createCustomerToken(String customerId);

    String createUserToken(int tokenVersion, String customerId, String userId, String username, List<String> roles);

    String getSubjectIdFromToken(String token);

    boolean isTokenValid(String token);

    Map<String, Object> getUserDetailsFromToken(String token);

    Claims getClaimsFromToken(String token);
}
