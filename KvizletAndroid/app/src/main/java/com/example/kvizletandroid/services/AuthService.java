package com.example.kvizletandroid.services;

import com.example.kvizletandroid.models.AuthRequest;
import com.example.kvizletandroid.models.JwtResponse;
import com.example.kvizletandroid.models.Pitanje;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthService {
    @POST("auth/register")
    Call<Void> registerUser(@Body AuthRequest authRequest);

    @POST("auth/authenticate")
    Call<JwtResponse> authenticateUser(@Body AuthRequest authRequest);


}
