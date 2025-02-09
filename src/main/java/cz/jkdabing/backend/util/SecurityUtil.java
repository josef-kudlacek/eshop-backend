package cz.jkdabing.backend.util;

import cz.jkdabing.backend.constants.ApiPathConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.security.CustomerDetails;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static UserEntity getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity userEntity) {
            return userEntity;
        }

        return null;
    }

    public static CustomerDetails getCurrentCustomerPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomerDetails customerDetails) {
            return customerDetails;
        }

        return null;
    }

    public static String extractToken(String token) {
        if (token != null && token.startsWith(JWTConstants.BEARER)) {
            return token.substring(7);
        }
        return null;
    }

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(ApiPathConstants.API_PATH);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(true);
        return cookie;
    }
}
