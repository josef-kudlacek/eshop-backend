package cz.jkdabing.backend.util;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class ConversionClassUtil {

    private ConversionClassUtil() {
    }

    public static SecretKey convertStringToSecretKey(String encodedKey) {
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
