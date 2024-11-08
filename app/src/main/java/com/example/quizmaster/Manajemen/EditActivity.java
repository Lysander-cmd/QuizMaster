package com.example.quizmaster.Manajemen;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Adapter.QuizEditAdapter;
import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuiz;
    private QuizEditAdapter quizEditAdapter;
    private List<QuizItem> quizList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        recyclerViewQuiz = findViewById(R.id.recyclerViewQuizList);
        recyclerViewQuiz.setLayoutManager(new LinearLayoutManager(this));
        quizEditAdapter = new QuizEditAdapter(quizList, this, quiz -> openDetailEditActivity(quiz.getId()));
        recyclerViewQuiz.setAdapter(quizEditAdapter);

        // Mengambil data kuis dari Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("kuis");
        fetchQuizData();

        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> {

            finish();
        });
    }

    // Mengambil data kuis dari Firebase dan mengupdate RecyclerView
    private void fetchQuizData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    QuizItem quiz = quizSnapshot.getValue(QuizItem.class);
                    if (quiz != null) {
                        quiz.setId(quizSnapshot.getKey()); // Mengatur ID dari key Firebase
                        quizList.add(quiz);
                        // Tambahkan log untuk melihat ID kuis yang diperoleh
                        Log.d("EditActivity", "Quiz ID: " + quiz.getId());
                    }
                }
                quizEditAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this, "Gagal memuat data kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi untuk membuka DetailEditActivity dengan ID kuis yang dipilih
    private void openDetailEditActivity(String quizId) {
        Intent intent = new Intent(this, DetailEditActivity.class);
        intent.putExtra("quizId", quizId);
        startActivity(intent);
    }
}