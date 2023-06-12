package ch.bbw.m183.vulnerapp.datamodel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @NotBlank(message = "username must not be empty") String username;

    @Column
    @NotBlank(message = "full name must not be empty") String fullname;

    @Column
    @NotBlank(message = "password must not be empty") String password;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
