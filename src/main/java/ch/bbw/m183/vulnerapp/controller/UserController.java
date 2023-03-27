package ch.bbw.m183.vulnerapp.controller;

import java.util.Base64;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import ch.bbw.m183.vulnerapp.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final LoginService loginService;

	@GetMapping("/whoami")
	public ResponseEntity<UserEntity> whoami(@RequestHeader("Authorization") String basicAuth) {
		var usernamePassword = new String(Base64.getDecoder().decode(basicAuth.substring("Basic ".length())));
		var arr = usernamePassword.split(":", 2);
		return loginService.whoami(arr[0], arr[1]);
	}
}
