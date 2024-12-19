package cz.jkdabing.backend.security.filter;

import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.CustomerDetailsService;
import cz.jkdabing.backend.security.jwt.JwtTokenFilter;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.MessageService;
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
public class SecurityConfigurationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerDetailsService customerDetailsService;

    private final UserRepository userRepository;

    private final MessageService messageService;

    public SecurityConfigurationFilter(
            JwtTokenProvider jwtTokenProvider,
            CustomerDetailsService customerDetailsService,
            UserRepository userRepository,
            MessageService messageService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDetailsService = customerDetailsService;
        this.userRepository = userRepository;
        this.messageService = messageService;
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
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenProvider, customerDetailsService, userRepository, messageService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
