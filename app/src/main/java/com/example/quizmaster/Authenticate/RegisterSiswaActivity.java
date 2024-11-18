package com.example.quizmaster.Authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.Controller.SignUpController;
import com.example.quizmaster.Database.DataPengguna;
import com.example.quizmaster.databinding.ActivityRegisterSiswaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterSiswaActivity extends AppCompatActivity {
    private ActivityRegisterSiswaBinding binding;
    private SignUpController signUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterSiswaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi SignUpController
        signUpController = new SignUpController(this);

        // Ambil role dari Intent
        String role = getIntent().getStringExtra("role");

        // Tombol registrasi
        binding.btnRegister.setOnClickListener(v -> {
            // Ambil input pengguna
            String username = binding.edtNama.getText().toString().trim();
            String noTelp = binding.edtNoTelp.getText().toString().trim();
            String email = binding.edtEmailRegister.getText().toString().trim();
            String password = binding.edtPasswordRegister.getText().toString().trim();
            String confirmPassword = binding.edtKonfirmasiPasswordRegister.getText().toString().trim();

            // Panggil metode dari SignUpController
            signUpController.fillFormRegistrasi(
                    username,
                    noTelp,
                    email,
                    password,
                    confirmPassword,
                    null,  // Tidak ada file untuk siswa
                    false, // isTeacher = false
                    role
            );
        });
    }

    @Override
    public void onBackPressed() {
        // Mencegah kembali ke layar registrasi, aplikasi akan keluar
        super.onBackPressed();
        finishAffinity(); // Menutup semua aktivitas yang ada di dalam task
    }
}