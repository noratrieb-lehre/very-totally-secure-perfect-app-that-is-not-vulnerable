package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class BasicConfiguration {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository.findById(username)
                .map(entity -> new User(entity.getUsername(), entity.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(basic -> basic.realmName("vulnerapp"))
                .csrf(cfg -> cfg.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/**").authenticated()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
