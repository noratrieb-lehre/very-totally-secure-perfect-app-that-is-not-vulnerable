package ch.bbw.m183.vulnerapp.controller;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login login) {
        return ResponseEntity.ok(this.loginService.login(login));
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserEntity> whoami(@AuthenticationPrincipal User user) {
        return loginService.whoami(user.getUsername());
    }

    public record LoginResponse(String token, String fullname) {}

    public record Login(String username, String password) {}
}
