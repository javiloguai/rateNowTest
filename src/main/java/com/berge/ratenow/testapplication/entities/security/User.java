package com.berge.ratenow.testapplication.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.berge.ratenow.testapplication.utils.json.CustomAuthorityDeserializer;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @Builder.Default
    @JsonIgnore
    private boolean accountNonExpired = true;
    @Builder.Default
    @JsonIgnore
    private boolean accountNonLocked = true;
    @Builder.Default
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean enabled = true;
    private String name;
    private String lastName;
    @Builder.Default
    private boolean admin = false;

    private String rolesAssigned;

    @Transient
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        if (rolesAssigned != null && !rolesAssigned.isEmpty()) {
            String[] split = rolesAssigned.split(",");
            List<SimpleGrantedAuthority> l = new ArrayList<>();
            for (String item : split) {
                l.add(new SimpleGrantedAuthority(item));
            }
            return l;
        }
        return new ArrayList<>();
    }

    @Transient
    public String getFullName() {
        return name + " " + lastName;
    }
}
