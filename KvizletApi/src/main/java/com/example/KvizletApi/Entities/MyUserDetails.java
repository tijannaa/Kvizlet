package com.example.KvizletApi.Entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities (you can customize this part based on roles if needed)
        return null; // You can return roles or authorities here
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return the user's password from the entity
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Return the user's username from the entity
    }

}