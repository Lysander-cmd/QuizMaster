package com.example.quizmaster.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quizmaster.Manajemen.EditActivity;
import com.example.quizmaster.Manajemen.TambahActivity;
import com.example.quizmaster.R;


public class QuizFragment extends Fragment {
    private Button btnTambahQuiz;
    private Button btnEditQuiz;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Initialize the button
        btnTambahQuiz = view.findViewById(R.id.btn_tambah_quiz);
        btnEditQuiz = view.findViewById(R.id.btn_edit_quiz);

        // Set OnClickListener on btnTambahQuiz
        btnTambahQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TambahActivity
                Intent intent = new Intent(getActivity(), TambahActivity.class);
                startActivity(intent);
            }
        });
        btnEditQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditActivity
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}