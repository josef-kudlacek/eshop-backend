package cz.jkdabing.backend.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenProvider {

    String createCustomerToken(String customerId);

    String createUserToken(UserDetails userDetails);

    String getCustomerIdFromToken(String token);

    boolean validateToken(String token);
}
