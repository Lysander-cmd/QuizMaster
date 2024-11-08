package com.example.quizmaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;

import java.util.List;


public class QuizHapusAdapter extends RecyclerView.Adapter<QuizHapusAdapter.QuizViewHolder> {
    private List<QuizItem> quizList;
    private Context context;
    private OnQuizDeleteListener onQuizDeleteListener;

    public interface OnQuizDeleteListener {
        void onDeleteClick(QuizItem quiz);
    }

    public QuizHapusAdapter(List<QuizItem> quizList, Context context, OnQuizDeleteListener onQuizDeleteListener) {
        this.quizList = quizList;
        this.context = context;
        this.onQuizDeleteListener = onQuizDeleteListener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_hapus, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizItem quiz = quizList.get(position);
        holder.textTitle.setText(quiz.getTitle());
        holder.textSubject.setText(quiz.getSubject());
        holder.textDuration.setText(quiz.getDuration());
        holder.textDifficulty.setText(quiz.getDifficulty());
        holder.imageQuiz.setImageResource(quiz.getImageResource());

        holder.buttonHapusQuiz.setOnClickListener(v -> {
            // Inflate custom layout for dialog
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_konfirmasi_hapus, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            // Get buttons in the custom layout
            Button buttonYes = dialogView.findViewById(R.id.buttonYes);
            Button buttonNo = dialogView.findViewById(R.id.buttonNo);

            AlertDialog dialog = builder.create();

            buttonYes.setOnClickListener(view -> {
                // Hapus item quiz jika "Iya" diklik
                onQuizDeleteListener.onDeleteClick(quiz);
                dialog.dismiss();
            });

            buttonNo.setOnClickListener(view -> {
                // Tutup dialog jika "Tidak" diklik
                dialog.dismiss();
            });

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSubject, textDuration, textDifficulty;
        ImageView imageQuiz;
        Button buttonHapusQuiz;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubject = itemView.findViewById(R.id.textSubject);
            textDuration = itemView.findViewById(R.id.textDuration);
            textDifficulty = itemView.findViewById(R.id.textDifficulty);
            imageQuiz = itemView.findViewById(R.id.imageQuiz);
            buttonHapusQuiz = itemView.findViewById(R.id.buttonHapusQuiz);
        }
    }
}
