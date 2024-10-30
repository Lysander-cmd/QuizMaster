package com.example.quizmaster.Authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizmaster.MainActivity;
import com.example.quizmaster.R;
import com.example.quizmaster.Role.RoleActivity;
import com.example.quizmaster.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        binding.tvToRole.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RoleActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmailLogin.getText().toString().trim();
            String password = binding.edtPasswordLogin.getText().toString().trim();

            // Validasi email
            if (email.isEmpty()) {
                binding.edtEmailLogin.setError("Email Harus Diisi");
                binding.edtEmailLogin.requestFocus();
                return;
            }

            // Validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtPasswordLogin.setError("Email Tidak Valid");
                binding.edtPasswordLogin.requestFocus();
                return;
            }

            // Validasi password
            if (password.isEmpty()) {
                binding.edtPasswordLogin.setError("Password Harus Diisi");
                binding.edtPasswordLogin.requestFocus();
                return;
            }

            selectAkun(email, password);
        });
    }

    private void selectAkun(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (email.equals("pengajar@gmail.com")) {
                            Toast.makeText(LoginActivity.this, "Selamat datang " + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Selamat datang " + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException() != null ? task.getException().getMessage() : "Login gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}