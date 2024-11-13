package com.example.quizmaster.Authenticate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizmaster.Controller.SignUpController;
import com.example.quizmaster.Database.DataPengguna;
import com.example.quizmaster.R;
import com.example.quizmaster.databinding.ActivityRegisterPengajarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterPengajarActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private ActivityRegisterPengajarBinding binding;
    private SignUpController signUpController;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPengajarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signUpController = new SignUpController(this);
        String role = getIntent().getStringExtra("role");
        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.edtNama.getText().toString();
            String noTelp = binding.edtNoTelp.getText().toString();
            String email = binding.edtEmailRegister.getText().toString();
            String password = binding.edtPasswordRegister.getText().toString();
            String confirmPassword = binding.edtKonfirmasiPasswordRegister.getText().toString();

            signUpController.fillFormRegistrasi(username, noTelp, email, password, confirmPassword, fileUri, false, role);
        });

        binding.btnUploadFile.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih File"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            binding.tvFileStatus.setText("File dipilih: " + fileUri.getLastPathSegment());
        }
    }
}