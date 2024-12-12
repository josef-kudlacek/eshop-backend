package cz.jkdabing.backend.util;

import cz.jkdabing.backend.constants.JWTConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String principal) {
            return principal;
        }

        return null;
    }

    public static String extractToken(String token) {
        if (token != null && token.startsWith(JWTConstants.BEARER)) {
            return token.substring(7);
        }
        return null;
    }
}
