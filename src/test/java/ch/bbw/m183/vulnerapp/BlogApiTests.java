package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.datamodel.BlogEntity;
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
public class BlogApiTests implements WithAssertions {

    @Autowired
    private WebTestClient webClient;

    @Test
    void getBlogsUnauthenticated() {
        this.webClient.get().uri("/api/blog").exchange().expectStatus().isOk();
    }

    @Test
    void createBlogUnauthenticated() {
        webClient.post()
                .uri("/api/blog")
                .bodyValue(new BlogEntity().setTitle("i hacked y'all").setBody("for real"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void createBlogAuthenticatedNoCsrf() {
        webClient.post()
                .uri("/api/blog")
                .header("Authorization", "")
                .bodyValue(new BlogEntity().setTitle("i hacked y'all").setBody("for real"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void getContainsCsrfTokenCookie() {
        webClient.get().uri("/api/blog").exchange().expectCookie().exists("XSRF-TOKEN");
    }

    @Test
    void createBlogAuthenticatedWithCsrfBadCredentials() {
        var csrfToken = "iJqUiW2G2gevMfz/xpk7fV1nLqYAeJUPNx4ZNHmx";

        webClient.post()
                .uri("/api/blog")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("evil:hacker".getBytes(StandardCharsets.UTF_8)))
                .header("X-XSRF-TOKEN", csrfToken)
                .cookie("XSRF-TOKEN", csrfToken)
                .bodyValue(new BlogEntity().setTitle("i am an evil hacker with the wrong password")
                        .setBody("yes yes yes"))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void createBlogAuthenticatedWithCsrfGoodCredentials() {
        var csrfToken = "iJqUiW2G2gevMfz/xpk7fV1nLqYAeJUPNx4ZNHmx";

        webClient.post()
                .uri("/api/blog")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("admin:super5ecret".getBytes(StandardCharsets.UTF_8)))
                .header("X-XSRF-TOKEN", csrfToken)
                .cookie("XSRF-TOKEN", csrfToken)
                .bodyValue(new BlogEntity().setTitle("i am the real admin").setBody("yes yes yes"))
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
