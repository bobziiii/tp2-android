package com.example.comfelix_nath_anprojetx;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Liste_destination extends AppCompatActivity {

    private TextView tvHistorique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_destination);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvHistorique = findViewById(R.id.tvHistorique);
        tvHistorique.setPaintFlags(tvHistorique.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvHistorique.setOnClickListener(v -> {
            Intent intent = new Intent(this, Historique.class);
            startActivity(intent);
        });
        
    }
}