package com.example.quizmaster.Manajemen;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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

    private EditText editTextJudulKuis, editTextPertanyaan, editTextJawaban,editTextMapel, editTextTime;
    private Spinner spinnerDifficult;
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
        buttonTambahKuis = findViewById(R.id.buttonSimpanKuis);

        // Inisialisasi Spinner dengan variabel kelas
        spinnerDifficult = findViewById(R.id.spinner_difficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficult.setAdapter(adapter);

        // Tambah listener untuk tombol tambah opsi
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

        // Listener tombol kembali
        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> finish());
    }

    // Fungsi untuk menambahkan opsi baru
    private void tambahOpsi() {
        // Cek apakah jumlah opsi sudah mencapai batas maksimum
        if (opsiCount > 4) {
            Toast.makeText(this, "Maksimal 4 opsi dapat ditambahkan!", Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayout opsiLayout = new LinearLayout(this);
        opsiLayout.setOrientation(LinearLayout.HORIZONTAL);
        RadioButton radioButton = new RadioButton(this);

        // Menambahkan EditText untuk opsi
        EditText editTextOpsi = new EditText(this);
        editTextOpsi.setHint("Opsi " + opsiCount);
        editTextOpsi.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        ));

        // Menyimpan EditText ke dalam daftar opsi
        opsiList.add(editTextOpsi);
        opsiCount++;

        // Menambahkan RadioButton dan EditText ke layout opsi
        opsiLayout.addView(radioButton);
        opsiLayout.addView(editTextOpsi);

        // Menambahkan layout opsi ke container utama
        opsiContainer.addView(opsiLayout);
    }

    // Fungsi untuk menyimpan data kuis baru ke Firebase
    private void simpanDataKuis() {
        String judulKuis = editTextJudulKuis.getText().toString().trim();
        String pertanyaan = editTextPertanyaan.getText().toString().trim();
        String jawaban = editTextJawaban.getText().toString().trim();
        String mataPelajaran = editTextMapel.getText().toString().trim();
        String waktu = editTextTime.getText().toString().trim();
        String level = spinnerDifficult.getSelectedItem().toString();


        if (judulKuis.isEmpty() || pertanyaan.isEmpty() || jawaban.isEmpty() || mataPelajaran.isEmpty() || waktu.isEmpty() || level.isEmpty()) {
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