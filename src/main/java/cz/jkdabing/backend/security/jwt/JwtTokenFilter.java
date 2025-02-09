package cz.jkdabing.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.constants.ResponseConstants;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.custom.InvalidJwtAuthenticationException;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.SecurityUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final MessageService messageService;

    public JwtTokenFilter(
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            UserRepository userRepository,
            MessageService messageService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException, RuntimeException {
        String token = extractToken(request);

        if (token != null) {
            if (!jwtTokenProvider.isTokenValid(token)) {
                prepareUnauthorizedResponse(response);
                return;
            } else {
                Map<String, Object> userDetails = jwtTokenProvider.getUserDetailsFromToken(token);
                if (userDetails.isEmpty()) {
                    handleCustomerToken(request, token, userDetails);
                } else {
                    try {
                        handleUserToken(request, userDetails);
                    } catch (InvalidJwtAuthenticationException exception) {
                        prepareUnauthorizedResponse(response);
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
        return SecurityUtil.extractToken(bearerToken);
    }

    private void handleCustomerToken(HttpServletRequest request, String token, Map<String, Object> userDetails) {
        String customerId = jwtTokenProvider.getSubjectIdFromToken(token);
        UserDetails customerDetails = userDetailsService.loadUserByUsername(customerId);

        if (customerDetails != null) {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(customerDetails.getAuthorities(), userDetails, token);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void handleUserToken(HttpServletRequest request, Map<String, Object> userDetails) {
        int tokenVersion = (int) userDetails.get(JWTConstants.TOKEN_VERSION);
        UUID userId = UUID.fromString((String) userDetails.get(JWTConstants.USER_ID));
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty() || tokenVersion != user.get().getTokenVersion()) {
            throw new InvalidJwtAuthenticationException(messageService.getMessage(JWTConstants.INVALID_TOKEN_MESSAGE));
        }

        Object rolesObj = userDetails.get(JWTConstants.ROLES);
        if (rolesObj instanceof List<?> rolesList) {
            List<String> roles = rolesList.stream()
                    .map(Object::toString)
                    .toList();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
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
