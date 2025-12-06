package com.eventos.configserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            @Value("${CONFIG_SERVER_USER:config}") String user,
            @Value("${CONFIG_SERVER_PASSWORD:config}") String password) {
        String effectiveUser = (user == null || user.isBlank()) ? "config" : user;
        String effectivePassword = (password == null || password.isBlank()) ? "config" : password;

        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(effectiveUser)
                .password(effectivePassword)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

}
