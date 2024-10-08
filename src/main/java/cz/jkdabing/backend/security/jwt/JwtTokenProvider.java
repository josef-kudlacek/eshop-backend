package cz.jkdabing.backend.security.jwt;

public interface JwtTokenProvider {

    String createToken(String customerId);

    String getCustomerIdFromToken(String token);

    boolean validateToken(String token);
}
