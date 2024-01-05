package com.board.api.confiq;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    CorsConfigurationSource corsConfigurationSource(){
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:5173")); //
            config.setAllowCredentials(true);
            return config;
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/app/auth/**").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.csrf().disable().authorizeHttpRequests()
//                .requestMatchers("/app/auth/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


}
