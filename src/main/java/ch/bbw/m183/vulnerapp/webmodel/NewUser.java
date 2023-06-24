package ch.bbw.m183.vulnerapp.webmodel;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NewUser(
        @NotBlank(message = "username must not be empty")
        String username,
        @NotBlank(message = "password must not be empty")
        @Length(min = 8)
        String password,
        @NotBlank(message = "full name must not be empty")

        String fullname) {
}
