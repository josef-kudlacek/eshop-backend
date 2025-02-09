package cz.jkdabing.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.constants.ResponseConstants;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.custom.InvalidJwtAuthenticationException;
import cz.jkdabing.backend.security.CustomUserDetailsService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    private final CustomUserDetailsService customUserDetailsService;

    private final MessageService messageService;

    public JwtTokenFilter(
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            CustomUserDetailsService customUserDetailsService,
            MessageService messageService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.customUserDetailsService = customUserDetailsService;
        this.messageService = messageService;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException, RuntimeException {

        String token = extractTokenFromCookies(ResponseConstants.COOKIE_ACCESS_TOKEN, request.getCookies());
        if (token != null) {
            if (!jwtTokenProvider.isTokenValid(token)) {
                prepareUnauthorizedResponse(response);
                return;
            } else {
                Map<String, Object> userClaims = jwtTokenProvider.getUserClaimsFromToken(token);
                if (userClaims.isEmpty()) {
                    handleCustomerToken(request, token);
                } else {
                    try {
                        handleUserToken(request, userClaims);
                    } catch (InvalidJwtAuthenticationException exception) {
                        prepareUnauthorizedResponse(response);
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(@NotEmpty String cookieTokenName, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieTokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private void handleCustomerToken(HttpServletRequest request, String token) {
        String customerId = jwtTokenProvider.getSubjectIdFromToken(token);
        UserDetails customerDetails = userDetailsService.loadUserByUsername(customerId);

        if (customerDetails != null) {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(customerDetails.getAuthorities(), customerDetails, token);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void handleUserToken(HttpServletRequest request, Map<String, Object> userClaims) {
        int tokenVersion = (int) userClaims.get(JWTConstants.TOKEN_VERSION);
        String userId = (String) userClaims.get(JWTConstants.USER_ID);
        UserEntity userEntity = customUserDetailsService.loadUserByUsername(userId);
        if (tokenVersion != userEntity.getTokenVersion()) {
            throw new InvalidJwtAuthenticationException(messageService.getMessage(JWTConstants.INVALID_TOKEN_MESSAGE));
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userEntity,
                null,
                userEntity.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void prepareUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(ResponseConstants.CHARACTER_ENCODING_UTF_8);
        response.setContentType(ResponseConstants.CONTENT_TYPE_APPLICATION_JSON);

        MessageResponse messageResponse = MessageResponse.builder()
                .message(messageService.getMessage(JWTConstants.INVALID_TOKEN_MESSAGE))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(messageResponse);
        response.getWriter().println(jsonResponse);
    }
}
