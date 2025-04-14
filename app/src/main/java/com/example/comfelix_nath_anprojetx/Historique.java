package com.example.comfelix_nath_anprojetx;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Historique extends AppCompatActivity {

    private ListView lvHistorique;
    private DatabaseHelper dbHelper;
    private ReservationAdapter adapter;
    private List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historique);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnRetour = findViewById(R.id.btnRetourHistorique);
        btnRetour.setOnClickListener(v -> finish());

        lvHistorique = findViewById(R.id.lvHistorique);
        dbHelper = new DatabaseHelper(this);
        reservations = dbHelper.getAllReservations();

        adapter = new ReservationAdapter(this, reservations, dbHelper);
        lvHistorique.setAdapter(adapter);
    }
}