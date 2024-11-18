package com.example.quizmaster;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailTipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tips);

        // Ambil data yang dikirim melalui Intent
        String judulTips = getIntent().getStringExtra("judulTips");
        String isiTips = getIntent().getStringExtra("isiTips");

        // Set data ke dalam view
        TextView judulTextView = findViewById(R.id.judulTipsDetail);
        TextView isiTextView = findViewById(R.id.isiTipsDetail);

        judulTextView.setText(judulTips);
        isiTextView.setText(isiTips);

        ImageView icArrowLeft = findViewById(R.id.ic_arrow_left);
        icArrowLeft.setOnClickListener(v -> {
            finish();
        });
    }
}