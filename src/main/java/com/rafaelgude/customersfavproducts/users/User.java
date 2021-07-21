package com.rafaelgude.customersfavproducts.users;

import com.rafaelgude.customersfavproducts.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Column(name = "role_id")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = Set.of(Role.USER);

}
