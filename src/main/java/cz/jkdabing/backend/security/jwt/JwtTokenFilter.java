package cz.jkdabing.backend.security.jwt;

import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.InvalidJwtAuthenticationException;
import cz.jkdabing.backend.repository.UserRepository;
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

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException, RuntimeException {
        String token = extractToken(request);

        if (token != null) {
            if (!jwtTokenProvider.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println(JWTConstants.INVALID_TOKEN_MESSAGE);
                return;
            } else {
                Map<String, Object> userDetails = jwtTokenProvider.getUserDetailsFromToken(token);
                if (userDetails.isEmpty()) {
                    handleCustomerToken(request, token, userDetails);
                } else {
                    try {
                        handleUserToken(request, userDetails);
                    } catch (InvalidJwtAuthenticationException exception) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().println(JWTConstants.INVALID_TOKEN_MESSAGE);
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(JWTConstants.BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
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
        String userName = (String) userDetails.get(JWTConstants.USERNAME);
        Optional<UserEntity> user = userRepository.findByUsername(userName);
        if (user.isEmpty() || tokenVersion != user.get().getTokenVersion()) {
            throw new InvalidJwtAuthenticationException("Invalid user token");
        }

        Object rolesObj = userDetails.get(JWTConstants.ROLES);
        if (rolesObj instanceof List<?> rolesList) {
            List<String> roles = rolesList.stream()
                    .map(Object::toString)
                    .toList();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
