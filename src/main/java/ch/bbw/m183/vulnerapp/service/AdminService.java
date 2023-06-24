package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.datamodel.Role;
import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.repository.RoleRepository;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import ch.bbw.m183.vulnerapp.webmodel.NewUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    public UserEntity createUser(NewUser newUser) throws InsecurePasswordException {
        log.info("Creating new user {}", newUser.username());
        if (newUser.password().chars().boxed().collect(Collectors.toSet()).size() < 4) {
            throw new InsecurePasswordException("Password must consist of at least 4 unique characters.");
        }

        var entity = new UserEntity()
                .setUsername(newUser.username())
                .setFullname(newUser.fullname())
                .setPassword(passwordEncoder.encode(newUser.password()));

        return userRepository.save(entity);
    }

    private void createUserFromEntity(UserEntity user) {
        log.info("Creating new user entity {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserEntity> getUsers(Pageable pageable) {
        log.info("Getting all users");
        return userRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(String username) {
        log.info("Deleting user {}", username);
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
                        .setRoles(List.of(admin, poster)),
                new UserEntity().setUsername("fuu")
                        .setFullname("Johanna Doe")
                        .setPassword("barbarfoo")
                        .setRoles(List.of(poster)),
                new UserEntity().setUsername("thesHyper7")
                        .setFullname("Desir Eh")
                        .setPassword("mBnXneOpktnfRqPMgf6SQxwsBVRqaH")
                        .setRoles(List.of(reader))
        ).forEach(this::createUserFromEntity);
    }

    Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            var role = new Role(name);
            roleRepository.save(role);
            return role;
        });
    }

    public static class InsecurePasswordException extends Exception {
        public InsecurePasswordException(String message) {
            super(message);
        }
    }
}
