package ch.bbw.m183.vulnerapp.service;

import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

import ch.bbw.m183.vulnerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HealthService {

	private final UserRepository userRepository;

	@SneakyThrows
	public String health(String host) {
		// lookup the local actuator health endpoint, and login as admin, to get extra details
		var admin = userRepository.getReferenceById("admin");
		var url = new URL("http://" + host + "/actuator/health"); // always connect to ourselves here
		var urlConnection = url.openConnection();
		var userpass = admin.getUsername() + ":" + admin.getPassword();
		var basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpass.getBytes());
		urlConnection.setRequestProperty("Authorization", basicAuth);
		var s = new Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
