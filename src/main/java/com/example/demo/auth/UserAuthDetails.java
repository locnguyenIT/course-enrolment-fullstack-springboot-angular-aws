package com.example.demo.auth;

import com.example.demo.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserAuthDetails implements UserDetails {

    private User user;

    public UserAuthDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRole().getName().name());
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().getName().name());
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnable();
    }
}
