package cz.jkdabing.backend.security.filter;

import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.CustomerDetailsService;
import cz.jkdabing.backend.security.handler.CustomAuthenticationEntryPoint;
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

import static cz.jkdabing.backend.constants.ApiPathConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurationFilter {

    private static final String ADMIN_PATHS = ADMIN_PATH;
    private static final String[] PUBLIC_PATHS = {
            CARTS_PATHS, CUSTOMERS, USERS_PATHS, IMAGES_PATHS, AUDIO_PATHS, PRODUCTS, PRODUCTS_PATHS
    };

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerDetailsService customerDetailsService;

    private final UserRepository userRepository;

    private final MessageService messageService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfigurationFilter(
            JwtTokenProvider jwtTokenProvider,
            CustomerDetailsService customerDetailsService,
            UserRepository userRepository,
            MessageService messageService,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDetailsService = customerDetailsService;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_PATHS)
                        .permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(LogoutConfigurer::permitAll)
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenProvider, customerDetailsService, userRepository, messageService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(authentication -> authentication
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
