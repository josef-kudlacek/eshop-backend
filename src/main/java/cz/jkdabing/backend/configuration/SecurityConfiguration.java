package cz.jkdabing.backend.configuration;

import cz.jkdabing.backend.security.CustomerDetailsService;
import cz.jkdabing.backend.security.JwtTokenFilter;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerDetailsService customerDetailsService;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider, CustomerDetailsService customerDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDetailsService = customerDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/customers", "/api/users/**", "/products/images/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .logout(LogoutConfigurer::permitAll)
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, customerDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
