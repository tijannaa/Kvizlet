package com.example.kvizletandroid;

import static com.example.kvizletandroid.Utils.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kvizletandroid.models.Pitanje;
import com.example.kvizletandroid.services.PitanjaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initViews();
        initListeners();
    }

    private void initListeners() {
        saveButton.setOnClickListener(v -> {
            String questionText = questionEditText.getText().toString().trim();
            String answerText = answerEditText.getText().toString().trim();

            if (questionText.isEmpty() || answerText.isEmpty()) {
                Toast.makeText(AddQuestionActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("JWT_TOKEN", "");
            String username = sharedPreferences.getString("USER_USERNAME", "");

            Pitanje newPitanje = new Pitanje();
            newPitanje.setQuestion(questionText);
            newPitanje.setAnswer(answerText);
            newPitanje.setUsername(username);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PitanjaService apiService = retrofit.create(PitanjaService.class);

            Call<Pitanje> call = apiService.addQuestion("Bearer " + token, newPitanje);
            call.enqueue(new Callback<Pitanje>() {
                @Override
                public void onResponse(Call<Pitanje> call, Response<Pitanje> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddQuestionActivity.this, "Question added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddQuestionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddQuestionActivity.this, "Failed to add question", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pitanje> call, Throwable t) {
                    Toast.makeText(AddQuestionActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initViews() {
        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        saveButton = findViewById(R.id.saveButton);
    }
}