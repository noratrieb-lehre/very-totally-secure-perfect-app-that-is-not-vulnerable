package ch.bbw.m183.vulnerapp.webmodel;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NewBlog(
        @NotBlank(message = "Title must not be empty")
        @Length(max = 100)
        String title,
        @NotBlank(message = "Body must not be empty")
        @Length(max = 1_000_000)
        String body) {
}
