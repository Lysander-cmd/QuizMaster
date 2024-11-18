package com.example.quizmaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.DetailTipsActivity;
import com.example.quizmaster.Model.DataTips;
import com.example.quizmaster.R;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {

    private List<DataTips> tipsList;
    private Context context;

    public TipsAdapter(List<DataTips> tipsList, Context context) {
        this.tipsList = tipsList;
        this.context = context;
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tips, parent, false);
        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        DataTips tips = tipsList.get(position);
        holder.judulTipsTextView.setText(tips.getJudulTips());
        holder.isiTipsTextView.setText(tips.getIsiTips());

        holder.pelajariButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTipsActivity.class);
            intent.putExtra("judulTips", tips.getJudulTips());
            intent.putExtra("isiTips", tips.getIsiTips());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tipsList.size();
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        TextView judulTipsTextView, isiTipsTextView;
        Button pelajariButton;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            judulTipsTextView = itemView.findViewById(R.id.judulTipsTextView);
            isiTipsTextView = itemView.findViewById(R.id.isiTipsTextView);
            pelajariButton = itemView.findViewById(R.id.pelajariButton);
        }
    }
}
