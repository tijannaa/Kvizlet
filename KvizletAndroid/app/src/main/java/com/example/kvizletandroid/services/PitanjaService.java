package com.example.kvizletandroid.services;

import com.example.kvizletandroid.models.Pitanje;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PitanjaService {
    @GET("questions/all/{username}")
    Call<List<Pitanje>> getAllQuestions(@Header("Authorization") String token, @Path("username") String username);

    @GET("questions/{id}")
    Call<Pitanje> getQuestionById(@Header("Authorization") String token, @Path("id") Long id);

    @DELETE("questions/{id}")
    Call<Void> deleteQuestion(@Header("Authorization") String token, @Path("id") Long id);

    @PUT("questions/{id}")
    Call<Pitanje> updateQuestion(@Header("Authorization") String token, @Path("id") Long id, @Body Pitanje question);

    @POST("questions")
    Call<Pitanje> addQuestion(@Header("Authorization") String token, @Body Pitanje question);

}
