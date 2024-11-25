package com.example.quizmaster.Tips;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizmaster.Model.DataTips;
import com.example.quizmaster.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TipsActivity extends AppCompatActivity {
    private EditText judulTipsEditText, tipsEditText;
    private Button postingButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        // Inisialisasi elemen UI
        judulTipsEditText = findViewById(R.id.judulTipsEditText);
        tipsEditText = findViewById(R.id.tipsEditText);
        postingButton = findViewById(R.id.postingButton);
        ImageView backButton = findViewById(R.id.ic_arrow_left);

        // Inisialisasi Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("tips");

        // Set tombol back
        backButton.setOnClickListener(v -> finish());

        // Set tombol posting
        postingButton.setOnClickListener(v -> postTips());
    }

    private void postTips() {
        String judulTips = judulTipsEditText.getText().toString().trim();
        String isiTips = tipsEditText.getText().toString().trim();

        // Validasi input
        if (judulTips.isEmpty()) {
            judulTipsEditText.setError("Judul tips tidak boleh kosong");
            judulTipsEditText.requestFocus();
            return;
        }
        if (judulTips.length() > 50) {
            Toast.makeText(this, "Judul maksimal 50 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isiTips.length() > 5000) {
            Toast.makeText(this, "Isi tips maksimal 200 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (isiTips.isEmpty()) {
            tipsEditText.setError("Isi tips tidak boleh kosong");
            tipsEditText.requestFocus();
            return;
        }

        // Buat ID unik untuk setiap tips
        String tipsId = databaseReference.push().getKey();

        // Simpan tips ke Firebase
        DataTips tips = new DataTips(tipsId, judulTips, isiTips);
        if (tipsId != null) {
            databaseReference.child(tipsId).setValue(tips)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(TipsActivity.this, "Tips berhasil diposting", Toast.LENGTH_SHORT).show();
                        // Mengkosongkan input setelah berhasil posting
                        judulTipsEditText.setText("");
                        tipsEditText.setText("");

                        // Menampilkan dialog berhasil posting
                        TampilSuksesDialog();
                    })
                    .addOnFailureListener(e -> Toast.makeText(TipsActivity.this, "Gagal memposting tips", Toast.LENGTH_SHORT).show());
        }
    }

    private void TampilSuksesDialog() {
        // Membuat dialog kustom
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_berhasil_posting);
        dialog.setCancelable(false);

        // Mengatur tombol kembali di dalam dialog
        Button buttonKembali = dialog.findViewById(R.id.button_kembali);
        buttonKembali.setOnClickListener(v -> {
            dialog.dismiss();
            // Kembali ke HomeFragment
            finish(); // Menutup TipsActivity agar kembali ke HomeFragment
        });

        // Menampilkan dialog
        dialog.show();
    }
}
