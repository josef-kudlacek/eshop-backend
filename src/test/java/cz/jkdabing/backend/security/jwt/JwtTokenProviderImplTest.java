package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.security.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtTokenProviderImplTest {

    @InjectMocks
    private JwtTokenProviderImpl jwtTokenProvider;

    @Mock
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        when(securityConfig.getJwtCustomerAccessTokenExpiration())
                .thenReturn(6000L);
        when(securityConfig.getSecretKey())
                .thenReturn(CustomerTestConstants.ENCODED_SECRET_KEY);

        UUID customerId = CustomerTestConstants.CUSTOMER_ID_UUID;
        String token = jwtTokenProvider.createCustomerToken(customerId.toString());

        assertNotNull(token);
        assertTrue(jwtTokenProvider.isTokenValid(token));
        assertEquals(customerId.toString(), jwtTokenProvider.getSubjectIdFromToken(token));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtTokenProvider.isTokenValid(invalidToken));
    }

}