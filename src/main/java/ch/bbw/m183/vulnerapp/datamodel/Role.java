package ch.bbw.m183.vulnerapp.datamodel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "name must not be empty")
    private String name;

    // @ManyToMany(mappedBy = "roles")
    // private Collection<UserEntity> users;
    public Role() {}

    public Role(String name) {
        this.name = name;
    }
}
