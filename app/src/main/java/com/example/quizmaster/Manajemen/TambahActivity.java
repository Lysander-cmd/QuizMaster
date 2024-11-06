package com.example.quizmaster.Manajemen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahActivity extends AppCompatActivity {

    private EditText editTextJudulKuis, editTextPertanyaan, editTextJawaban,editTextMapel, editTextTime, editTextDifficult;
    private LinearLayout opsiContainer;
    private Button buttonTambahKuis;
    private DatabaseReference databaseReference;
    private ArrayList<EditText> opsiList = new ArrayList<>();
    private int opsiCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        // Inisialisasi Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("kuis");

        // Inisialisasi Views
        editTextJudulKuis = findViewById(R.id.editTextJudulKuis);
        editTextPertanyaan = findViewById(R.id.editTextPertanyaan);
        opsiContainer = findViewById(R.id.opsiContainer);
        editTextJawaban = findViewById(R.id.editTextJawaban);
        editTextMapel = findViewById(R.id.editTextMapel);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDifficult = findViewById(R.id.editTextDifficult);
        buttonTambahKuis = findViewById(R.id.buttonSimpanKuis);

        Button buttonTambahOpsi = findViewById(R.id.buttonTambahOpsi);
        buttonTambahOpsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahOpsi();  // Menambahkan opsi baru
            }
        });

        // Tambah listener untuk tombol tambah kuis
        buttonTambahKuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanDataKuis();
            }
        });
    }

    // Fungsi untuk menambahkan opsi baru
    private void tambahOpsi() {
        LinearLayout opsiLayout = new LinearLayout(this);
        opsiLayout.setOrientation(LinearLayout.HORIZONTAL);

        RadioButton radioButton = new RadioButton(this);
        EditText editTextOpsi = new EditText(this);
        editTextOpsi.setHint("Opsi " + opsiCount);
        editTextOpsi.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        ));
        opsiList.add(editTextOpsi);
        opsiCount++;

        opsiLayout.addView(radioButton);
        opsiLayout.addView(editTextOpsi);
        opsiContainer.addView(opsiLayout);
    }

    // Fungsi untuk menyimpan data kuis baru ke Firebase
    private void simpanDataKuis() {
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
                opsi.add(opsiText);
            }
        }

        Map<String, Object> kuisData = new HashMap<>();
        kuisData.put("judulKuis", judulKuis);
        kuisData.put("pertanyaan", pertanyaan);
        kuisData.put("opsi", opsi);
        kuisData.put("jawaban", jawaban);
        kuisData.put("mataPelajaran", mataPelajaran);
        kuisData.put("waktu", waktu);
        kuisData.put("level", level);

        String kuisId = databaseReference.push().getKey();
        databaseReference.child(kuisId).setValue(kuisData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tampilkanDialogBerhasil();
            } else {
                Toast.makeText(TambahActivity.this, "Gagal menambahkan kuis", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi untuk menampilkan dialog berhasil
    private void tampilkanDialogBerhasil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_berhasil, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Button buttonKembali = dialogView.findViewById(R.id.button_kembali);
        buttonKembali.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();  // Menutup activity setelah dialog ditutup
        });

        alertDialog.show();
    }
}