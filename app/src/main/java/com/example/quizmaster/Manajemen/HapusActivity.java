package com.example.quizmaster.Manajemen;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Adapter.QuizHapusAdapter;
import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HapusActivity extends AppCompatActivity {
    private RecyclerView recyclerViewQuiz;
    private QuizHapusAdapter quizHapusAdapter;
    private List<QuizItem> quizList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus);

        // Inisialisasi RecyclerView dan Adapter
        recyclerViewQuiz = findViewById(R.id.recyclerViewQuizList);
        recyclerViewQuiz.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("kuis");

        quizHapusAdapter = new QuizHapusAdapter(quizList, this, quiz -> {
            // Menghapus kuis dari Firebase
            databaseReference.child(quiz.getId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(HapusActivity.this, "Kuis berhasil dihapus", Toast.LENGTH_SHORT).show();
                    quizList.remove(quiz);
                    quizHapusAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HapusActivity.this, "Gagal menghapus kuis", Toast.LENGTH_SHORT).show();
                }
            });
        });

        recyclerViewQuiz.setAdapter(quizHapusAdapter);

        // Memuat data dari Firebase ke dalam RecyclerView
        fetchQuizData();

        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> {
            // Kembali ke fragment sebelumnya
            finish();
        });
    }

    private void fetchQuizData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    QuizItem quiz = quizSnapshot.getValue(QuizItem.class);
                    if (quiz != null) {
                        quiz.setId(quizSnapshot.getKey());
                        quizList.add(quiz);
                    }
                }
                quizHapusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HapusActivity.this, "Gagal memuat data kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }
}