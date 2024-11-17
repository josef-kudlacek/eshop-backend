package cz.jkdabing.backend.configuration;

import cz.jkdabing.backend.security.CustomerDetailsService;
import cz.jkdabing.backend.security.JwtTokenFilter;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerDetailsService customerDetailsService;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider, CustomerDetailsService customerDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDetailsService = customerDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //TODO: Read ROLE from token in JwtTokenFilter
        return httpSecurity.authorizeHttpRequests(request -> request
                        .requestMatchers("/api/admin").hasRole("USER")
                        .requestMatchers("/api/customers", "api/users/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .logout(LogoutConfigurer::permitAll)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, customerDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
