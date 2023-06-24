package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.controller.UserController;
import ch.bbw.m183.vulnerapp.datamodel.Role;
import ch.bbw.m183.vulnerapp.repository.UserRepository;
import ch.bbw.m183.vulnerapp.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public UserController.LoginResponse login(UserController.Login login) {
        var userEntity = userRepository.findById(login.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (passwordEncoder.matches(login.password(), userEntity.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", login.username());

            String authorities = userEntity.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));
            claims.put("roles", authorities);

            String jwt = jwtHelper.createJwtForClaims(login.username(), claims);
            return new UserController.LoginResponse(jwt, userEntity.getFullname());
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
}
