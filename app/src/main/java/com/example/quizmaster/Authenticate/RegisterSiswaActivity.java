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
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterSiswaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();  // Inisialisasi FirebaseAuth
        signUpController = new SignUpController(this);

        String role = getIntent().getStringExtra("role");  // Ambil role dari Intent

        // Dapatkan userId hanya jika pengguna saat ini tidak null
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(userId);

            // Simpan role ke database
            userRef.child("role").setValue(role);
        } else {
            Toast.makeText(this, "Pengguna tidak terautentikasi.", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.edtNama.getText().toString();
            String noTelp = binding.edtNoTelp.getText().toString();
            String email = binding.edtEmailRegister.getText().toString();
            String password = binding.edtPasswordRegister.getText().toString();
            String confirmPassword = binding.edtKonfirmasiPasswordRegister.getText().toString();

            signUpController.fillFormRegistrasi(username, noTelp, email, password, confirmPassword, null, false, role); // Tambahkan role sebagai parameter
        });
    }
}