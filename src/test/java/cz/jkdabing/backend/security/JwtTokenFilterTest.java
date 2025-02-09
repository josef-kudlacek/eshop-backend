package cz.jkdabing.backend.security;

import cz.jkdabing.backend.BackendApplication;
import cz.jkdabing.backend.TestFactory;
import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.constant.JwtTestConstants;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
class JwtTokenFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testValidToken() throws Exception {
        when(jwtTokenProvider.isTokenValid(anyString()))
                .thenReturn(true);
        when(jwtTokenProvider.getSubjectIdFromToken((anyString())))
                .thenReturn(CustomerTestConstants.TOKEN);

        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidToken() throws Exception {
        String invalidJwtToken = JwtTestConstants.INVALID_JWT_TOKEN;
        when(jwtTokenProvider.isTokenValid(invalidJwtToken))
                .thenReturn(false);

        Cookie cookie = TestFactory.prepareCookie(JwtTestConstants.COOKIE_ACCESS_TOKEN, invalidJwtToken, JwtTestConstants.EXPIRATION_TIME);

        mockMvc.perform(get("/api/")
                        .cookie(cookie))
                .andExpect(status().isUnauthorized());
    }
}