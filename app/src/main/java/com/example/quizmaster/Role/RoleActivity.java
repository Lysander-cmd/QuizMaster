package com.example.quizmaster.Role;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.Authenticate.RegisterPengajarActivity;
import com.example.quizmaster.Authenticate.RegisterSiswaActivity;
import com.example.quizmaster.R;
import com.example.quizmaster.databinding.ActivityRoleBinding;

public class RoleActivity extends AppCompatActivity {

    private ActivityRoleBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final String[] selectedRole = {null};

        binding.pengajar.setOnClickListener(v -> {
            selectedRole[0] = "pengajar";
            updateCardViewSelection(binding.pengajar, binding.siswa);
        });

        binding.siswa.setOnClickListener(v -> {
            selectedRole[0] = "siswa";
            updateCardViewSelection(binding.siswa, binding.pengajar);
        });
        binding.btnLogout.setOnClickListener(v -> {
            if ("pengajar".equals(selectedRole[0])) {
                Intent intent = new Intent(RoleActivity.this, RegisterPengajarActivity.class);
                intent.putExtra("role", "pengajar"); // Mengirim data role ke RegisterActivity
                startActivity(intent);
            } else if ("siswa".equals(selectedRole[0])) {
                Intent intent = new Intent(RoleActivity.this, RegisterSiswaActivity.class);
                intent.putExtra("role", "siswa"); // Mengirim data role ke RegisterActivity
                startActivity(intent);
            }
        });
    }

    private void updateCardViewSelection(View selectedCard, View unselectedCard) {
        selectedCard.setBackgroundResource(R.drawable.selected_card_background);
        unselectedCard.setBackgroundResource(R.drawable.unselected_card_background);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}