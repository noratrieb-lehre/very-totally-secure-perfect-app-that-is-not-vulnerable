package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.datamodel.Role;
import ch.bbw.m183.vulnerapp.repository.RoleRepository;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyAwesomeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Trying to load {}", username);
        var user = userRepository.findById(username)
                .map(entity -> new User(entity.getUsername(),
                        entity.getPassword(),
                        this.getAuthorities(entity.getRoles())
                ))
                .orElseGet(() -> {
                    log.info("Could not find user, falling back to empty user.");
                    return new User(
                            " ",
                            " ",
                            this.getAuthorities(List.of(this.roleRepository.findByName("READER")
                                    .orElseThrow(() -> new RuntimeException(("READER role not found")))))
                    );
                });

        log.info("Successfully loaded user {}", user.getUsername());

        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).toList();
    }
}
