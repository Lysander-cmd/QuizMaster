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

public class RegisterSiswaActivity extends AppCompatActivity {
    private ActivityRegisterSiswaBinding binding;
    private SignUpController signUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterSiswaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signUpController = new SignUpController(this);

        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.edtNama.getText().toString();
            String noTelp = binding.edtNoTelp.getText().toString();
            String email = binding.edtEmailRegister.getText().toString();
            String password = binding.edtPasswordRegister.getText().toString();
            String confirmPassword = binding.edtKonfirmasiPasswordRegister.getText().toString();

            signUpController.fillFormRegistrasi(username, noTelp, email, password, confirmPassword, null, false);
        });
    }
}
