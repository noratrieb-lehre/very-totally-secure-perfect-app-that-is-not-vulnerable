package ch.bbw.m183.vulnerapp;

import ch.bbw.m183.vulnerapp.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Service
public class TestHelper {
    @Autowired
    private WebTestClient webClient;

    public String login(String username, String password) {
        var result = this.webClient.post().uri("/api/user/login")
                .bodyValue(new UserController.Login(username, password)).exchange();
        result.expectStatus().isOk();
        var body = result.returnResult(UserController.LoginResponse.class).getResponseBody().blockFirst();
        return "Bearer " + Objects.requireNonNull(body, "Returned body was null").token();
    }
}
