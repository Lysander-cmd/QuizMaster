package com.example.quizmaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Model.QuizItem;
import com.example.quizmaster.R;

import java.util.List;


    public class QuizEditAdapter extends RecyclerView.Adapter<QuizEditAdapter.QuizViewHolder> {

        private List<QuizItem> quizList;
        private Context context;
        private OnQuizEditListener onQuizEditListener;

        public interface OnQuizEditListener {
            void onEditClick(QuizItem quiz);
        }

        public QuizEditAdapter(List<QuizItem> quizList, Context context, OnQuizEditListener onQuizEditListener) {
            this.quizList = quizList;
            this.context = context;
            this.onQuizEditListener = onQuizEditListener;
        }

        @NonNull
        @Override
        public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_edit, parent, false);
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
            holder.buttonEditQuiz.setOnClickListener(v -> onQuizEditListener.onEditClick(quiz));
        }

        @Override
        public int getItemCount() {
            return quizList.size();
        }

        public static class QuizViewHolder extends RecyclerView.ViewHolder {
            TextView textTitle, textSubject, textDuration, textDifficulty;
            ImageView imageQuiz;
            Button buttonEditQuiz;

            public QuizViewHolder(@NonNull View itemView) {
                super(itemView);
                textTitle = itemView.findViewById(R.id.textTitle);
                textSubject = itemView.findViewById(R.id.textSubject);
                textDuration = itemView.findViewById(R.id.textDuration);
                textDifficulty = itemView.findViewById(R.id.textDifficulty);
                imageQuiz = itemView.findViewById(R.id.imageQuiz);
                buttonEditQuiz = itemView.findViewById(R.id.buttonEditQuiz);
            }
        }
    }

