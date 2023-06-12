package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.datamodel.Role;
import ch.bbw.m183.vulnerapp.repository.RoleRepository;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class MyAwesomeUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAwesomeUserDetailsService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findById(username)
                .map(entity -> new User(entity.getUsername(), entity.getPassword(), this.getAuthorities(entity.getRoles())))
                .orElseGet(() -> new User(" ", " ", this.getAuthorities(List.of(this.roleRepository.findByName("READER")
                        .orElseThrow(() -> new RuntimeException(("READER role not found")))))));

        LOGGER.error("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
        LOGGER.error("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
        LOGGER.error(user.getUsername());
        LOGGER.error(Arrays.toString(user.getAuthorities().toArray()));
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
