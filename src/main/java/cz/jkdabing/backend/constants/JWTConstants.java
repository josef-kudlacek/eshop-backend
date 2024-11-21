package cz.jkdabing.backend.constants;

public class JWTConstants {

    public static final String BEARER = "Bearer ";
    public static final String ROLES = "roles";
    public static final String USERNAME = "username";
    public static final String TOKEN_VERSION = "tokenVersion";

    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";

    private JWTConstants() {
    }
}
