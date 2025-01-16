package ru.pstu.polls_system.file_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.pstu.polls_system.file_service.business.aspect.SecurityPermissionEvaluator;
import ru.pstu.polls_system.file_service.web.security.CustomAuthenticationProvider;
import ru.pstu.polls_system.file_service.web.security.JwtTokenAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(GET, getSwaggerPatterns()).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenAuthenticationFilter(authenticationProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public SecurityPermissionEvaluator securityPermissionEvaluator() {
        return new SecurityPermissionEvaluator();
    }

    private static String[] getSwaggerPatterns() {
        return new String[]{
                "/swagger-ui.html",
                "/swagger-ui.html/**",
                "/swagger-ui",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
        };
    }
}
