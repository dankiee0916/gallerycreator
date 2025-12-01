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
                .requestMatchers(
                        "/",                 // home page
                        "/register", "/register/**",
                        "/login", "/logout",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/uploads/**",       // served uploaded files
                        "/galleries/all",    // public "View All Galleries" page
                        "/galleries/*",      // public view of a single gallery by id
                        "/photos/view/**",   // public view of a single photo
                        "/error"             // Spring Boot error page
                ).permitAll()
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

        
        return http.build();
    }

    // Bean to expose the authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
