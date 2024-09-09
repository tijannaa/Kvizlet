package com.example.kvizletandroid.adapters;

import static com.example.kvizletandroid.Utils.BASE_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvizletandroid.EditQuestionActivity;
import com.example.kvizletandroid.R;
import com.example.kvizletandroid.models.Pitanje;
import com.example.kvizletandroid.services.PitanjaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PitanjeAdapter extends RecyclerView.Adapter<PitanjeAdapter.ViewHolder> {

    private List<Pitanje> pitanjeList;
    private Context context;

    public PitanjeAdapter(List<Pitanje> pitanjeList, Context context) {
        this.pitanjeList = pitanjeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pitanje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pitanje pitanje = pitanjeList.get(position);
        holder.questionTextView.setText(pitanje.getQuestion());
        holder.answerTextView.setText(pitanje.getAnswer());

        holder.editImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditQuestionActivity.class);
            intent.putExtra("QUESTION_ID", pitanje.getId());
            context.startActivity(intent);
        });

        holder.deleteImageView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete this question?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call delete API and remove from the list
                        deleteQuestion(pitanje.getId());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteQuestion(Long id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("JWT_TOKEN", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PitanjaService apiService = retrofit.create(PitanjaService.class);

        Call<Void> call = apiService.deleteQuestion("Bearer " + token, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Remove question from the list and notify adapter
                    pitanjeList.removeIf(question -> question.getId().equals(id));
                    notifyDataSetChanged();
                    Toast.makeText(context, "Question deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete question", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pitanjeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answerTextView;
        ImageView editImageView;
        ImageView deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }
    }
}
