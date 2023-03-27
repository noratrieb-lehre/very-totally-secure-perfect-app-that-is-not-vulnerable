package ch.bbw.m183.vulnerapp.controller;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin123") // noone will ever guess!
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/create")
	public UserEntity createUser(UserEntity newUser) {
		return adminService.createUser(newUser);
	}

	@GetMapping("/users")
	public Page<UserEntity> getUsers(Pageable pageable) {
		return adminService.getUsers(pageable);
	}

	@GetMapping("/delete/{username}")
	public void deleteUser(@PathVariable String username) {
		adminService.deleteUser(username);
	}
}
