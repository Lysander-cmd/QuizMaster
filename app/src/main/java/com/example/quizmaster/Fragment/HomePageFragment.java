package com.example.quizmaster.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quizmaster.Adapter.QuizAdapter;
import com.example.quizmaster.Adapter.TipsAdapter;
import com.example.quizmaster.Model.DataTips;
import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;
import com.example.quizmaster.Tips.TipsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {
    private RecyclerView recyclerViewQuiz;
    private QuizAdapter quizAdapter;
    private List<QuizItem> quizList;
    private RecyclerView recyclerViewTips;
    private TipsAdapter tipsAdapter;
    private List<DataTips> tipsList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerViewQuiz = view.findViewById(R.id.recyclerViewQuiz);
        recyclerViewQuiz.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewTips = view.findViewById(R.id.recyclerViewTips);
        recyclerViewTips.setLayoutManager(new LinearLayoutManager(getContext()));

        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizList, getContext());
        recyclerViewQuiz.setAdapter(quizAdapter);

        tipsList = new ArrayList<>();
        tipsAdapter = new TipsAdapter(tipsList, getContext());
        recyclerViewTips.setAdapter(tipsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        fetchQuizData();
        fetchTipsData();

        // Inisialisasi tombol "tambah tips" dan set OnClickListener
        ImageButton tambahTipsButton = view.findViewById(R.id.tambahTipsButton);
        tambahTipsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchQuizData() {
        DatabaseReference quizRef = databaseReference.child("kuis");
        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    try {
                        String id = quizSnapshot.getKey();
                        String judulKuis = quizSnapshot.child("judulKuis").getValue(String.class);
                        String mataPelajaran = quizSnapshot.child("mataPelajaran").getValue(String.class);
                        String waktu = quizSnapshot.child("waktu").getValue(String.class);
                        String level = quizSnapshot.child("level").getValue(String.class);
                        String pertanyaan = quizSnapshot.child("pertanyaan").getValue(String.class);
                        String jawaban = quizSnapshot.child("jawaban").getValue(String.class);

                        ArrayList<String> opsi = new ArrayList<>();
                        DataSnapshot opsiSnapshot = quizSnapshot.child("opsi");
                        for (DataSnapshot optionSnapshot : opsiSnapshot.getChildren()) {
                            String option = optionSnapshot.getValue(String.class);
                            if (option != null) {
                                opsi.add(option);
                            }
                        }

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

    private void fetchTipsData() {
        DatabaseReference tipsRef = databaseReference.child("tips");
        tipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tipsList.clear();
                for (DataSnapshot tipsSnapshot : snapshot.getChildren()) {
                    try {
                        String id = tipsSnapshot.getKey();
                        String judulTips = tipsSnapshot.child("judulTips").getValue(String.class);
                        String isiTips = tipsSnapshot.child("isiTips").getValue(String.class);
                        DataTips tips = new DataTips(id, judulTips, isiTips);
                        tipsList.add(tips);
                    } catch (Exception e) {
                        Toast.makeText(getContext(),
                                "Error parsing tips data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                tipsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "Gagal memuat data tips: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
