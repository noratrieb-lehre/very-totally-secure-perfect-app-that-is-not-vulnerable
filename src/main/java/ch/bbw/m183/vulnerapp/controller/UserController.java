package ch.bbw.m183.vulnerapp.controller;

import ch.bbw.m183.vulnerapp.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login login) {
        return ResponseEntity.ok(this.loginService.login(login));
    }

    public record LoginResponse(String token, String fullname) {
    }

    public record Login(String username, String password) {
    }
}
