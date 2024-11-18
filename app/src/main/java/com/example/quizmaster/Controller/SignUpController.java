package com.example.quizmaster.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.quizmaster.Database.DataPengguna;
import com.example.quizmaster.Authenticate.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpController {
    private FirebaseAuth auth;
    private DataPengguna db;
    private Context context;

    public SignUpController(Context context) {
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
        this.db = new DataPengguna();
    }

    public void fillFormRegistrasi(String username, String noTelp, String email, String password, String confirmPassword, @Nullable Uri fileUri, boolean isTeacher, String role) {
        // Validasi nomor telepon
        if (noTelp.isEmpty() || noTelp.length() < 10) {
            Toast.makeText(context, "Nomor Telepon Tidak Valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi email
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Email Tidak Valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi password
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(context, "Password Minimal 6 Karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirmPassword.equals(password)) {
            Toast.makeText(context, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek apakah file diperlukan untuk pengajar
        if (isTeacher && fileUri == null) {
            Toast.makeText(context, "File harus diunggah untuk Pengajar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mulai registrasi
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Simpan role ke database
                        String userId = auth.getCurrentUser().getUid();
                        db.saveUserData(userId, username, noTelp, role);

                        if (isTeacher && fileUri != null) {
                            // Upload file jika pengajar
                            navigateToLogin();
                        } else {
                            // Simpan data tanpa file untuk siswa
                            navigateToLogin();
                        }
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadFileToFirebaseStorage(String userId, String username, String noTelp, Uri fileUri, String role) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/" + System.currentTimeMillis());
        UploadTask uploadTask = storageReference.putFile(fileUri);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Simpan data pengguna termasuk role setelah file berhasil diupload
                    db.saveUserData(userId, username, noTelp, role);
                    navigateToLogin();
                });
            } else {
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Toast.makeText(context, "Register Berhasil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}