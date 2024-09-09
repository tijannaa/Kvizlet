package com.example.kvizletandroid.services;

import com.example.kvizletandroid.models.AuthRequest;
import com.example.kvizletandroid.models.JwtResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/register")
    Call<Void> registerUser(@Body AuthRequest authRequest);

    @POST("auth/authenticate")
    Call<JwtResponse> authenticateUser(@Body AuthRequest authRequest);
}
