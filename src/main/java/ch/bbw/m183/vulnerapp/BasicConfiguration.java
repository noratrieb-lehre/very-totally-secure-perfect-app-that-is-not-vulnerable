package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.repository.PrivilegeRepository;
import ch.bbw.m183.vulnerapp.repository.RoleRepository;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import ch.bbw.m183.vulnerapp.service.MyAwesomeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class BasicConfiguration {
    /*
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository.findById(username)
                .map(entity -> new User(entity.getUsername(), entity.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
    */

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        return new MyAwesomeUserDetailsService(userRepository, roleRepository, privilegeRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName(null);

        return http.httpBasic(basic -> basic.realmName("vulnerapp"))
                .csrf(cfg ->
                        cfg.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(handler)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.GET, "/api/blog")
                                .permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
