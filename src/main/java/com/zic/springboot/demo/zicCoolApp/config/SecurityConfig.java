package com.zic.springboot.demo.zicCoolApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //The Customizer.withDefaults() here is needed since by default spring security will
        //be reached before the controller, so the request will be block because of
        //CORS if we don't add this filter here. It tells Spring to use a bean with type
        //CorsConfigurationSource for the CORS configuration.
        http.cors(withDefaults()).securityMatcher("/api/**", "/actuator/**")
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                    .requestMatchers("/actuator/**").hasRole("ADMIN")
            );
        //Use Http basic Authentication
        http.httpBasic(withDefaults());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Allow all origins
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allow specific HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Allow specific headers
        configuration.setAllowCredentials(false); // Allow credentials (e.g., cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply the configuration to all paths

        return source;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails zic = User.builder().username("zic")
                .password("{noop}lizichong081")
                .roles("ADMIN")
                .build();
        UserDetails haoyu = User.builder().username("haoyu")
                .password("{noop}haoyu001")
                .roles("FRIEND")
                .build();
        UserDetails pinyuan = User.builder().username("pinyuan")
                .password("{noop}pinyuan001")
                .roles("FRIEND")
                .build();
        return new InMemoryUserDetailsManager(zic, haoyu, pinyuan);
    }
}

