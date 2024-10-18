package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.constant.CustomerTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderImplTest {

    @InjectMocks
    private JwtTokenProviderImpl jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", CustomerTestConstants.ENCODED_SECRET_KEY);
    }

    @Test
    void testGenerateToken() {
        UUID customerId = CustomerTestConstants.ID;
        String token = jwtTokenProvider.createToken(customerId.toString());

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(customerId.toString(), jwtTokenProvider.getCustomerIdFromToken(token));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

}