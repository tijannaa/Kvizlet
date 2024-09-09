package com.example.kvizletandroid;

import static com.example.kvizletandroid.Utils.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvizletandroid.adapters.PitanjeAdapter;
import com.example.kvizletandroid.models.Pitanje;
import com.example.kvizletandroid.services.AuthService;
import com.example.kvizletandroid.services.PitanjaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button logoutButton;
    private ImageView addImageView;

    private PitanjeAdapter pitanjeAdapter;
    private PitanjaService pitanjaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pitanjaService = retrofit.create(PitanjaService.class);

        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", null);
        String username = sharedPreferences.getString("USER_USERNAME", null);

        if (token != null && username != null) {
            // Make request to get all questions
            Call<List<Pitanje>> call = pitanjaService.getAllQuestions("Bearer " + token, username);
            call.enqueue(new Callback<List<Pitanje>>() {
                @Override
                public void onResponse(Call<List<Pitanje>> call, Response<List<Pitanje>> response) {
                    if (response.isSuccessful()) {
                        List<Pitanje> pitanja = response.body();
                        pitanjeAdapter = new PitanjeAdapter(pitanja, MainActivity.this);
                        recyclerView.setAdapter(pitanjeAdapter);
                    } else if (response.code() == 401) {
                        // Unauthorized, redirect to login
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Pitanje>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            // Token or username is null, redirect to login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



    }

    private void initListeners() {
        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        addImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddQuestionActivity.class); // Make sure AddQuestionActivity exists
            startActivity(intent);
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        logoutButton = findViewById(R.id.logoutButton);
        addImageView = findViewById(R.id.addImageView);
    }
}