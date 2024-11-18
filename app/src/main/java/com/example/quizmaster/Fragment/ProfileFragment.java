package com.example.quizmaster.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizmaster.Authenticate.LoginActivity;
import com.example.quizmaster.Database.DataPengguna;
import com.example.quizmaster.R;
import com.example.quizmaster.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private final DataPengguna userDatabase = new DataPengguna();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            userDatabase.readUsername(auth.getCurrentUser().getUid(), binding.edtName);
            binding.edtEmail.setText(auth.getCurrentUser().getEmail());
        }

        binding.btnLogout.setOnClickListener(v -> btnLogout());
    }

    private void btnLogout() {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
