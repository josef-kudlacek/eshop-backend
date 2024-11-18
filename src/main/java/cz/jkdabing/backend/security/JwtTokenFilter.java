package cz.jkdabing.backend.security;

import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.security.jwt.JwtAuthenticationToken;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
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

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {
            if (!jwtTokenProvider.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Invalid or expired token");
                return;
            } else {
                Map<String, Object> userDetails = jwtTokenProvider.getUserDetailsFromToken(token);
                if (userDetails.isEmpty()) {
                    handleCustomerToken(request, token, userDetails);
                } else {
                    handleUserToken(request, userDetails);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
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
        String username = (String) userDetails.get(JWTConstants.USERNAME);
        Object rolesObj = userDetails.get(JWTConstants.ROLES);
        if (rolesObj instanceof List<?> rolesList) {
            List<String> roles = rolesList.stream()
                    .map(Object::toString)
                    .toList();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
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
