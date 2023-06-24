package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
@Transactional
@RequiredArgsConstructor
public class HealthService {

    private final ServletWebServerApplicationContext ctx;
    private final JwtHelper jwtHelper;

    @SneakyThrows
    public String health() {
        System.out.println(ctx.getWebServer().getPort());

        // create an admin JWT
        Map<String, String> claims = new HashMap<>();
        claims.put("username", "admin");
        claims.put("roles", "ADMIN");
        String jwt = jwtHelper.createJwtForClaims("admin", claims);

        var url = new URL("http://localhost:" + ctx.getWebServer().getPort() + "/actuator/health"); // always connect to ourselves here
        var urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Bearer " + jwt);
        var s = new Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
