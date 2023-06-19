package ch.bbw.m183.vulnerapp.datamodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "blogs")
public class BlogEntity {

    @Id
    UUID id;

    @Column
    @CreationTimestamp
    @NotNull(message = "created date must be specified") LocalDateTime createdAt;

    @Column(columnDefinition = "text")
    @NotBlank(message = "Title must not be empty") String title;

    @Column(columnDefinition = "text")
    @NotBlank(message = "Body must not be empty") String body;
}
