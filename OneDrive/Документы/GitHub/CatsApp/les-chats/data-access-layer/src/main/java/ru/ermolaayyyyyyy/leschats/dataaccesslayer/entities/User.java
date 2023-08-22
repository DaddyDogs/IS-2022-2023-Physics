package ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name="Users")
public class User implements UserDetails {
    public User(Owner owner, String login, String password, Role role){
        this.owner = owner;
        this.login = login;
        this.password = password;
        this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.MERGE)
    private Owner owner;
    private Role role;
    private String login;
    private String password;
    private boolean enabled = true;

    public void update(String login, String password){
        if (login != null){
            setLogin(login);
        }
        if (password != null){
            setPassword(password);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }
}
