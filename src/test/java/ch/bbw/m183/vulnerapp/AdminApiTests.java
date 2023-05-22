package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AutoConfigureMockMvc
@SpringBootTest
public class AdminApiTests implements WithAssertions {
    @Autowired
    private WebTestClient webClient;

    @Test
    void createUserUnauthenticated() {
        this.webClient.post()
                .uri("/api/admin123/create")
                .bodyValue(new UserEntity().setUsername("testuser").setPassword("not5secret").setFullname("test user"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void createUserAuthenticatedNoCsrf() {
        webClient.post()
                .uri("/api/admin123/create")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .bodyValue(new UserEntity().setUsername("testuser").setPassword("not5secret").setFullname("test user"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void createUserAuthenticated() {
        var csrfToken = "iJqUiW2G2gevMfz/xpk7fV1nLqYAeJUPNx4ZNHmx";

        webClient.post()
                .uri("/api/admin123/create")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .header("X-XSRF-TOKEN", csrfToken)
                .cookie("XSRF-TOKEN", csrfToken)
                .bodyValue(new UserEntity().setUsername("testuser").setPassword("not5secret").setFullname("test user"))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void getUsersUnauthenticated() {
        this.webClient.get()
                .uri("/api/admin123/users")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void getUsersAuthenticated() {
        webClient.get()
                .uri("/api/admin123/users")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void deleteUserUnauthenticated() {
        this.webClient.delete()
                .uri("/api/admin123/delete/fuu")
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void deleteUserAuthenticatedNoCsrf() {
        webClient.delete()
                .uri("/api/admin123/delete/fuu")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void deleteUserAuthenticated() {
        var csrfToken = "iJqUiW2G2gevMfz/xpk7fV1nLqYAeJUPNx4ZNHmx";

        webClient.delete()
                .uri("/api/admin123/delete/fuu")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .header("X-XSRF-TOKEN", csrfToken)
                .cookie("XSRF-TOKEN", csrfToken)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
