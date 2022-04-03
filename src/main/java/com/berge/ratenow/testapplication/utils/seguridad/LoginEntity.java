package com.berge.ratenow.testapplication.utils.seguridad;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author jruizh
 *
 */
public class LoginEntity extends User {

    private String name;
    private String lastName;
    private boolean admin;
    private boolean enabled;

    public LoginEntity(String username, String password, boolean admin, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
