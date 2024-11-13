package com.example.quizmaster.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizmaster.Adapter.QuizAdapter;
import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomePageSiswaFragment extends Fragment {
    private RecyclerView recyclerViewQuiz;
    private QuizAdapter quizAdapter;
    private List<QuizItem> quizList;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerViewQuiz = view.findViewById(R.id.recyclerViewQuiz);
        recyclerViewQuiz.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewQuiz);

        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizList, getContext());
        recyclerViewQuiz.setAdapter(quizAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("kuis");
        fetchQuizData();
        return view;
    }

    private void fetchQuizData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    try {
                        // Ambil data mentah
                        String id = quizSnapshot.getKey();
                        String judulKuis = quizSnapshot.child("judulKuis").getValue(String.class);
                        String mataPelajaran = quizSnapshot.child("mataPelajaran").getValue(String.class);
                        String waktu = quizSnapshot.child("waktu").getValue(String.class);
                        String level = quizSnapshot.child("level").getValue(String.class);
                        String pertanyaan = quizSnapshot.child("pertanyaan").getValue(String.class);
                        String jawaban = quizSnapshot.child("jawaban").getValue(String.class);

                        // Ambil opsi
                        ArrayList<String> opsi = new ArrayList<>();
                        DataSnapshot opsiSnapshot = quizSnapshot.child("opsi");
                        for (DataSnapshot optionSnapshot : opsiSnapshot.getChildren()) {
                            String option = optionSnapshot.getValue(String.class);
                            if (option != null) {
                                opsi.add(option);
                            }
                        }

                        // Buat objek QuizItem
                        QuizItem quiz = new QuizItem(id, judulKuis, mataPelajaran, waktu,
                                level, pertanyaan, jawaban, opsi, R.drawable.quiz);
                        quizList.add(quiz);
                    } catch (Exception e) {
                        Toast.makeText(getContext(),
                                "Error parsing quiz data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "Gagal memuat data kuis: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
