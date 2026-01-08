package com.dankirent.api.model.user;

import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserGroup> userGroups = new HashSet<UserGroup>();

    public User(UserRequestDto dto) {
        this.firstName = dto.firstName();
        this.lastName = dto.lastName();
        this.cpf = dto.cpf();
        this.phoneNumber = dto.phone();
        this.email = dto.email();
        this.password = dto.password();
    }
}
