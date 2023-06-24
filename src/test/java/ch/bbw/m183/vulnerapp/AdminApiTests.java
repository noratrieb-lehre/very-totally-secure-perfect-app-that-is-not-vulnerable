package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.datamodel.UserEntity;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureMockMvc
@SpringBootTest
public class AdminApiTests implements WithAssertions {
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private TestHelper testHelper;

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
    void createUserAuthenticated() {
        var authToken = testHelper.login("admin", "super5ecret");

        webClient.post()
                .uri("/api/admin123/create")
                .header("Authorization", authToken)
                .bodyValue(new UserEntity().setUsername("testuser").setPassword("not5secret").setFullname("test user"))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void createUserNoPerms() {
        var authToken = testHelper.login("thesHyper7", "mBnXneOpktnfRqPMgf6SQxwsBVRqaH");

        webClient.post()
                .uri("/api/admin123/create")
                .header("Authorization", authToken)
                .bodyValue(new UserEntity().setUsername("testuser").setPassword("not5secret").setFullname("test user"))
                .exchange()
                .expectStatus()
                .isForbidden();
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
        var authToken = testHelper.login("admin", "super5ecret");

        webClient.get()
                .uri("/api/admin123/users")
                .header("Authorization", authToken)
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
    void deleteUserAuthenticated() {
        var authToken = testHelper.login("admin", "super5ecret");

        webClient.delete()
                .uri("/api/admin123/delete/fuu")
                .header("Authorization", authToken)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
