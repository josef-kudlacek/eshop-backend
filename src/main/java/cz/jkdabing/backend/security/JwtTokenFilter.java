package cz.jkdabing.backend.security;

import cz.jkdabing.backend.security.jwt.JwtAuthenticationToken;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
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
                    String customerId = jwtTokenProvider.getCustomerIdFromToken(token);
                    UserDetails customerDetails = userDetailsService.loadUserByUsername(customerId);

                    if (customerDetails != null) {
                        JwtAuthenticationToken authentication = new JwtAuthenticationToken(customerDetails.getAuthorities(), userDetails, token);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    String username = (String) userDetails.get("username");
                    Object rolesObj = userDetails.get("roles");
                    if (rolesObj instanceof List<?> rolesList) {
                        List<String> roles = rolesList.stream()
                                .map(Object::toString)
                                .toList();

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                roles.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        );
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
