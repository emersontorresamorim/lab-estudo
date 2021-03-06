package br.com.labestudo.api.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {

    private static final long serialVersionUID = 1L;

    public AuthUser(br.com.labestudo.api.auth.model.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPass(), authorities);
    }
}
