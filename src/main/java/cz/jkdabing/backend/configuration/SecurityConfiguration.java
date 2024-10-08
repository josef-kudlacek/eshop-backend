package cz.jkdabing.backend.configuration;

import cz.jkdabing.backend.security.CustomerDetailsService;
import cz.jkdabing.backend.security.JwtTokenFilter;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

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
        return httpSecurity.authorizeHttpRequests(request -> request
                        .requestMatchers(new AntPathRequestMatcher("/auth/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .authenticated()
                )
                .logout(LogoutConfigurer::permitAll)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, customerDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, password, is_active from users where user_id = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, user_type from users where user_id = ?");

        return jdbcUserDetailsManager;
    }
}
