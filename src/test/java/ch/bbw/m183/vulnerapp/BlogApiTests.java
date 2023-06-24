package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.datamodel.BlogEntity;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogApiTests implements WithAssertions {

    @Autowired
    private WebTestClient webClient;
    @Autowired
    private TestHelper testHelper;

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
    void createBlogAuthenticatedNoPerms() {
        var authToken = testHelper.login("thesHyper7", "mBnXneOpktnfRqPMgf6SQxwsBVRqaH");

        webClient.post()
                .uri("/api/blog")
                .header("Authorization", authToken)
                .bodyValue(new BlogEntity().setTitle("i am an evil hacker with no permissions")
                        .setBody("yes yes yes"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void createBlogAuthenticatedWithCsrfGoodCredentials() {
        var authToken = testHelper.login("admin", "super5ecret");

        webClient.post()
                .uri("/api/blog")
                .header("Authorization", authToken)
                .bodyValue(new BlogEntity().setTitle("i am the real admin").setBody("yes yes yes"))
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
