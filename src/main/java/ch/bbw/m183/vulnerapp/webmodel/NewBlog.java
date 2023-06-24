package ch.bbw.m183.vulnerapp.webmodel;

import jakarta.validation.constraints.NotBlank;

public record NewBlog(@NotBlank(message = "Title must not be empty") String title,
                      @NotBlank(message = "Body must not be empty") String body) {
}
