package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.datamodel.Role;
import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.repository.RoleRepository;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    public UserEntity createUser(UserEntity newUser) {
        if (newUser.getPassword().chars().boxed().collect(Collectors.toSet()).size() < 4) {
            throw new IllegalArgumentException("Password must consist of at least 4 unique characters.");
        }

        return userRepository.save(newUser.setPassword(passwordEncoder.encode(newUser.getPassword())));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserEntity> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadTestUsers() {
        var admin = createRoleIfNotFound("ADMIN");
        var poster = createRoleIfNotFound("POSTER");
        var reader = createRoleIfNotFound("READER");

        Stream.of(
                new UserEntity().setUsername("admin")
                        .setFullname("Super Admin")
                        .setPassword("super5ecret")
                        .setRoles(List.of(admin)),
                new UserEntity().setUsername("fuu")
                        .setFullname("Johanna Doe")
                        .setPassword("barbarfoo")
                        .setRoles(List.of(poster)),
                new UserEntity().setUsername("thesHyper7")
                        .setFullname("Desir Eh")
                        .setPassword("m/u5wvGRaUX1XBnXneOpktnfRqPCnG7+ogXFUv6Mgf6SQxws8Rq2hoMEKLZEdIHoE0qsbEvgTdBVRqaH")
                        .setRoles(List.of(reader))
        ).forEach(this::createUser);
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            var role = new Role(name);
            roleRepository.save(role);
            return role;
        });
    }
}
