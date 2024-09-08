package com.example.KvizletApi.Controllers;

import com.example.KvizletApi.Utils.JwtUtil;
import com.example.KvizletApi.Entities.User;
import com.example.KvizletApi.Services.UserService; // Make sure you have a UserService for handling user creation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService; // Service for user management

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtUtil.generateToken(userDetails);
        }

        throw new AuthenticationException("Invalid credentials") {};
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        // Ensure to create the user with the provided credentials
        User newUser = new User();
        newUser.setUsername(authRequest.getUsername());
        newUser.setPassword(authRequest.getPassword()); // You should encode the password
        userService.save(newUser); // Save the new user to the database

        return "User registered successfully";
    }
}