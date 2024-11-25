package com.example.quizmaster.Manajemen;

import android.os.Bundle;

import com.example.quizmaster.Model.QuizItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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

    private EditText editTextJudulKuis, editTextPertanyaan, editTextJawaban, editTextMapel, editTextTime;
    private Spinner spinnerDifficulty;
    private TextView textTitle, textSubject, textDuration, textDifficulty;
    private Button buttonUpdateKuis, buttonTambahOpsi;
    private DatabaseReference databaseReference;
    private LinearLayout opsiContainer;  // Menambahkan wadah untuk opsi
    private String quizId;
    private ArrayList<EditText> opsiList = new ArrayList<>();  // Daftar opsi yang ada
    private int opsiCount = 0;

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
        buttonTambahOpsi = findViewById(R.id.buttonTambahOpsi);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
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
        buttonTambahOpsi.setOnClickListener(v -> {
            if (opsiCount >= 4) {
                Toast.makeText(this, "Maksimal 4 opsi dapat ditambahkan!", Toast.LENGTH_SHORT).show();
            } else {
                tambahOpsi("");  // Tambah opsi baru dengan teks kosong
            }
        });

        // Menambahkan listener untuk tombol update
        buttonUpdateKuis.setOnClickListener(v -> updateDataKuis());

        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> {
            finish();
        });
        spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDifficulty = parent.getItemAtPosition(position).toString();
                Log.d("SpinnerSelection", "Selected Difficulty: " + selectedDifficulty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
                    String difficulty = quiz.getDifficulty();
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            DetailEditActivity.this,
                            R.array.difficulty_levels,
                            android.R.layout.simple_spinner_item
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDifficulty.setAdapter(adapter);

                    // Cari index nilai spinner yang sesuai dengan difficulty
                    int spinnerPosition = adapter.getPosition(difficulty);
                    spinnerDifficulty.setSelection(spinnerPosition);
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
        opsiCount++;
    }

    // Fungsi untuk memperbarui data kuis di Firebase
    private void updateDataKuis() {
        String judulKuis = editTextJudulKuis.getText().toString().trim();
        String pertanyaan = editTextPertanyaan.getText().toString().trim();
        String jawaban = editTextJawaban.getText().toString().trim();
        String mataPelajaran = editTextMapel.getText().toString().trim();
        String waktu = editTextTime.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        if (judulKuis.isEmpty() || pertanyaan.isEmpty() || jawaban.isEmpty() || mataPelajaran.isEmpty() || waktu.isEmpty() || difficulty.isEmpty()) {
            Toast.makeText(this, "Pastikan semua data sudah terisi!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (judulKuis.length() > 50) {
            Toast.makeText(this, "Judul maksimal 50 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pertanyaan.length() > 200) {
            Toast.makeText(this, "Pertanyaan maksimal 200 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jawaban.length() > 200) {
            Toast.makeText(this, "Jawabam maksimal 200 karakter!", Toast.LENGTH_SHORT).show();
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
        kuisData.put("difficulty", difficulty);
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