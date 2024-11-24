package com.ktb.eatbookappbackend.config;

import com.ktb.eatbookappbackend.oauth.CustomOAuth2MemberService;
import com.ktb.eatbookappbackend.oauth.OAuth2SuccessHandler;
import com.ktb.eatbookappbackend.oauth.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomOAuth2MemberService principalOauth2MemberService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Qualifier("customCorsConfigurationSource")
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2MemberService))
            )
            .build();
    }
}
