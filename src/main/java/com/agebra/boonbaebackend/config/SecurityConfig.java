package com.agebra.boonbaebackend.config;

import com.agebra.boonbaebackend.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
  securedEnabled = true,
  jsr250Enabled = true)
public class SecurityConfig {

    private String[] whiteList = {
      "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/error",
      "/api/v1/users/login", "/api/v1/users/id/*/exists", "/api/v1/users/username/*/exists", "/api/v1/users/referrers",
      "/api/v1/search/ranking",
      "/api/v1/recycling/*", "/api/v1/tip/",
      "/api/v1/comments/*/reports",
      "/api/v1/recycling/*", 
      "/api/v1/funding/category", "/api/v1/token/validate"
    };

    private String[] adminList = {
      "/api/v1/recycling_confirm/**","api/v1/qna/*/reply", "/api/v1/comments"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                  .httpBasic().disable()
                  .cors().configurationSource(corsConfigurationSource())
          .and()

          .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(whiteList).permitAll()
                //.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/recycling/*/comments").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/qna/*").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/v1/recycling/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/trees/").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/v1/recycling").hasAuthority("ADMIN")
                .requestMatchers(adminList).hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
            .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS 허용 적용
    @Bean //--------- (2)
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost");
//        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        //configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
