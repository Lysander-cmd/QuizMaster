package com.example.quizmaster.Manajemen;

import android.os.Bundle;

import com.example.quizmaster.Model.QuizItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.quizmaster.databinding.ActivityDetailEditBinding;

import com.example.quizmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DetailEditActivity extends AppCompatActivity {

    private EditText editTextJudulKuis, editTextPertanyaan, editTextJawaban, editTextMapel, editTextTime, editTextDifficult;
    private Button buttonUpdateKuis;
    private DatabaseReference databaseReference;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);

        // Inisialisasi Views
        editTextJudulKuis = findViewById(R.id.editTextJudulKuis);
        editTextPertanyaan = findViewById(R.id.editTextPertanyaan);
        editTextJawaban = findViewById(R.id.editTextJawaban);
        editTextMapel = findViewById(R.id.editTextMapel);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDifficult = findViewById(R.id.editTextDifficult);
        buttonUpdateKuis = findViewById(R.id.buttonUpdateQuiz);

        // Mengambil quizId dari Intent
        quizId = getIntent().getStringExtra("quizId");

        if (quizId != null) {
            loadQuizData(quizId); // Load data kuis dari Firebase
        }

        // Menambahkan listener untuk tombol update
        buttonUpdateKuis.setOnClickListener(v -> updateDataKuis());
    }

    // Memuat data kuis dari Firebase berdasarkan quizId
    private void loadQuizData(String quizId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("kuis").child(quizId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QuizItem quiz = snapshot.getValue(QuizItem.class);
                if (quiz != null) {
                    // Set data ke EditText untuk diedit
                    editTextJudulKuis.setText(quiz.getTitle());
                    editTextPertanyaan.setText(quiz.getQuestion());
                    editTextJawaban.setText(quiz.getAnswer());
                    editTextMapel.setText(quiz.getSubject());
                    editTextTime.setText(quiz.getDuration());
                    editTextDifficult.setText(quiz.getDifficulty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailEditActivity.this, "Gagal memuat data kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi untuk memperbarui data kuis di Firebase
    private void updateDataKuis() {
        String judulKuis = editTextJudulKuis.getText().toString().trim();
        String pertanyaan = editTextPertanyaan.getText().toString().trim();
        String jawaban = editTextJawaban.getText().toString().trim();
        String mataPelajaran = editTextMapel.getText().toString().trim();
        String waktu = editTextTime.getText().toString().trim();
        String level = editTextDifficult.getText().toString().trim();

        if (judulKuis.isEmpty() || pertanyaan.isEmpty() || jawaban.isEmpty() || mataPelajaran.isEmpty() || waktu.isEmpty() || level.isEmpty()) {
            Toast.makeText(this, "Pastikan semua data sudah terisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> kuisData = new HashMap<>();
        kuisData.put("judulKuis", judulKuis);
        kuisData.put("pertanyaan", pertanyaan);
        kuisData.put("jawaban", jawaban);
        kuisData.put("mataPelajaran", mataPelajaran);
        kuisData.put("waktu", waktu);
        kuisData.put("level", level);

        databaseReference.updateChildren(kuisData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DetailEditActivity.this, "Kuis berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(DetailEditActivity.this, "Gagal memperbarui kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }
}