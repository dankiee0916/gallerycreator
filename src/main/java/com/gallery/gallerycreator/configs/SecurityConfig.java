package com.gallery.gallerycreator.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Bean for password encoding (used to hash and check passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public access allowed for these paths
                .requestMatchers("/", "/register", "/register/**", "/login", "/logout", "/css/**", "/images/**", "galleries/all","/uploads/**", "/js/**").permitAll()
                // All other requests require login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                      // Custom login page
                .defaultSuccessUrl("/galleries", true)    // After successful login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")        // Redirect after logout
                .permitAll()
            );
        // CSRF is enabled by default -> add hidden token in forms
        return http.build();
    }

    // Bean to expose the authentication manager (used internally by Spring Security)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
