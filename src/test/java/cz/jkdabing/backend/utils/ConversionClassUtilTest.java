package cz.jkdabing.backend.utils;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class ConversionClassUtilTest {

    @Test
    void testConvertStringToSecretKey() {
        String encodedKey = "f1I/NHdYBvX8D0l3nXsd3bzoPQzjRzp5Fqqol6yKX0chUZvIhc4ZsfotlkPQGgxdesDYSLrxOog==";
        SecretKey secretKey = ConversionClassUtil.convertStringToSecretKey(encodedKey);

        assertNotNull(secretKey);
    }

    @Test
    void testConvertStringToSecretKey_weakKeyException() {
        String encodedKey = "f1I/";

        WeakKeyException exception = assertThrows(
                WeakKeyException.class,
                () -> ConversionClassUtil.convertStringToSecretKey(encodedKey)
        );

        String startOfErrorMessage =
                "The specified key byte array is 32 bits which is not secure enough for any JWT HMAC-SHA algorithm.";

        assertTrue(exception.getMessage().startsWith(startOfErrorMessage));
    }
}