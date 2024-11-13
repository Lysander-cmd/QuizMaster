package com.example.quizmaster.Manajemen;

import android.os.Bundle;

import com.example.quizmaster.Model.QuizItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailEditActivity extends AppCompatActivity {

    private EditText editTextJudulKuis, editTextPertanyaan, editTextJawaban, editTextMapel, editTextTime, editTextDifficult;
    private TextView textTitle, textSubject, textDuration, textDifficulty;
    private Button buttonUpdateKuis;
    private DatabaseReference databaseReference;
    private LinearLayout opsiContainer;  // Menambahkan wadah untuk opsi
    private String quizId;
    private ArrayList<EditText> opsiList = new ArrayList<>();  // Daftar opsi yang ada

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

        // CardView elements
        textTitle = findViewById(R.id.textTitle);
        textSubject = findViewById(R.id.textSubject);
        textDuration = findViewById(R.id.textDuration);
        textDifficulty = findViewById(R.id.textDifficulty);

        // Inisialisasi LinearLayout untuk opsi
        opsiContainer = findViewById(R.id.opsiContainer);

        // Mengambil quizId dari Intent
        quizId = getIntent().getStringExtra("quizId");
        if (quizId != null) {
            loadQuizData(quizId);
        }

        // Menambahkan listener untuk tombol update
        buttonUpdateKuis.setOnClickListener(v -> updateDataKuis());

        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> {
            finish();
        });
    }

    // Memuat data kuis dari Firebase berdasarkan quizId
    private void loadQuizData(String quizId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("kuis").child(quizId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QuizItem quiz = snapshot.getValue(QuizItem.class);
                if (quiz != null) {
                    // Mengatur data ke dalam EditText untuk diedit
                    editTextJudulKuis.setText(quiz.getTitle());
                    editTextPertanyaan.setText(quiz.getQuestion());
                    editTextJawaban.setText(quiz.getAnswer());
                    editTextMapel.setText(quiz.getSubject());
                    editTextTime.setText(quiz.getDuration());
                    editTextDifficult.setText(quiz.getDifficulty());

                    // Mengatur data ke dalam CardView
                    textTitle.setText(quiz.getTitle());
                    textSubject.setText(quiz.getSubject());
                    textDuration.setText("Waktu: " + quiz.getDuration());
                    textDifficulty.setText("Level: " + quiz.getDifficulty());

                    // Mengambil dan menampilkan opsi
                    List<String> opsi = quiz.getOpsi(); // Ambil opsi dari Firebase
                    if (opsi != null) {
                        for (String opsiText : opsi) {
                            tambahOpsi(opsiText);  // Menambahkan opsi yang sudah ada
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailEditActivity.this, "Gagal memuat data kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menambahkan opsi ke dalam LinearLayout
    private void tambahOpsi(String opsiText) {
        LinearLayout opsiLayout = new LinearLayout(this);
        opsiLayout.setOrientation(LinearLayout.HORIZONTAL);

        RadioButton radioButton = new RadioButton(this);
        EditText editTextOpsi = new EditText(this);
        editTextOpsi.setText(opsiText);  // Menampilkan opsi yang sudah ada
        editTextOpsi.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        ));
        opsiList.add(editTextOpsi);

        opsiLayout.addView(radioButton);
        opsiLayout.addView(editTextOpsi);
        opsiContainer.addView(opsiLayout);
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

        ArrayList<String> opsi = new ArrayList<>();
        for (EditText editTextOpsi : opsiList) {
            String opsiText = editTextOpsi.getText().toString().trim();
            if (!opsiText.isEmpty()) {
                opsi.add(opsiText);  // Menyimpan opsi yang dimodifikasi
            }
        }

        Map<String, Object> kuisData = new HashMap<>();
        kuisData.put("judulKuis", judulKuis);
        kuisData.put("pertanyaan", pertanyaan);
        kuisData.put("jawaban", jawaban);
        kuisData.put("mataPelajaran", mataPelajaran);
        kuisData.put("waktu", waktu);
        kuisData.put("level", level);
        kuisData.put("opsi", opsi);  // Menyimpan opsi yang sudah dimodifikasi

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