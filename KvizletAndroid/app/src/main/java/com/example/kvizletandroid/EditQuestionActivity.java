package com.example.kvizletandroid;

import static com.example.kvizletandroid.Utils.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kvizletandroid.models.Pitanje;
import com.example.kvizletandroid.services.PitanjaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditQuestionActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button saveButton;
    private Long questionId;
    private String authToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        initViews();
        initListeners();

        questionId = getIntent().getLongExtra("QUESTION_ID", -1L);

        if (questionId == -1L) {
            Toast.makeText(this, "Invalid question ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Retrieve the authorization token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        authToken = sharedPreferences.getString("JWT_TOKEN", null);

        if (authToken == null) {
            Toast.makeText(this, "Authorization token not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the current question details
        fetchQuestionDetails(questionId);


    }

    private void initListeners() {
        saveButton.setOnClickListener(v -> {
            String updatedQuestion = questionEditText.getText().toString().trim();
            String updatedAnswer = answerEditText.getText().toString().trim();

            if (updatedQuestion.isEmpty() || updatedAnswer.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Pitanje updatedPitanje = new Pitanje();
            updatedPitanje.setId(questionId);
            updatedPitanje.setQuestion(updatedQuestion);
            updatedPitanje.setAnswer(updatedAnswer);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PitanjaService apiService = retrofit.create(PitanjaService.class);

            Call<Pitanje> call = apiService.updateQuestion("Bearer " + authToken, questionId, updatedPitanje);
            call.enqueue(new Callback<Pitanje>() {
                @Override
                public void onResponse(Call<Pitanje> call, Response<Pitanje> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditQuestionActivity.this, "Question updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditQuestionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditQuestionActivity.this, "Failed to update question", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pitanje> call, Throwable t) {
                    Toast.makeText(EditQuestionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initViews() {
        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        saveButton = findViewById(R.id.saveButton);
    }

    private void fetchQuestionDetails(Long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PitanjaService apiService = retrofit.create(PitanjaService.class);

        Call<Pitanje> call = apiService.getQuestionById("Bearer " + authToken, id);
        call.enqueue(new Callback<Pitanje>() {
            @Override
            public void onResponse(Call<Pitanje> call, Response<Pitanje> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pitanje pitanje = response.body();
                    questionEditText.setText(pitanje.getQuestion());
                    answerEditText.setText(pitanje.getAnswer());
                } else {
                    Toast.makeText(EditQuestionActivity.this, "Failed to fetch question details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pitanje> call, Throwable t) {
                Toast.makeText(EditQuestionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}