package ch.bbw.m183.vulnerapp.controller;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.service.AdminService;
import ch.bbw.m183.vulnerapp.webmodel.NewUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin123") // noone will ever guess!
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody @Valid NewUser newUser) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createUser(newUser));
        } catch (AdminService.InsecurePasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public Page<UserEntity> getUsers(Pageable pageable) {
        return adminService.getUsers(pageable);
    }

    @DeleteMapping("/delete/{username}")
    public void deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
    }
}
