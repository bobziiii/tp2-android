package com.example.comfelix_nath_anprojetx;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Liste_destination extends AppCompatActivity {

    private TextView tvHistorique;
    private Spinner spBudget, spDate, spType;
    private EditText editTextSearchBar;
    private ListView lvListeDestinations;

    private ListeDestinationsDAO dao;
    private ListeDestinationsAdapter adapter;
    private List<Voyage> listeVoyages = new ArrayList<>();
    private List<Voyage> listeVoyagesFiltrees = new ArrayList<>();


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

        editTextSearchBar = findViewById(R.id.editTextSearchBar);
        lvListeDestinations = findViewById(R.id.lvListeDestinations);
        tvHistorique = findViewById(R.id.tvHistorique);
        tvHistorique.setPaintFlags(tvHistorique.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //aller à l'historique
        tvHistorique.setOnClickListener(v -> {
            Intent intent = new Intent(this, Historique.class);
            startActivity(intent);
        });

        //spinner filtres
        spBudget = findViewById(R.id.spBudget);
        spDate = findViewById(R.id.spDate);
        spType = findViewById(R.id.spType);
        String[] budgets = {"Budget", "0 - 200", "201 - 400", "401 - 600", "601 - 800"};
        String[] dates = {"Date", "2025-03", "2025-04", "2025-05", "2025-06", "2025-07", "2025-08", "2025-09"};
        String[] types = {"Type", "Culturel", "Aventure", "Nature", "Bien-être", "Gastronomique"};
        spBudget.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, budgets));
        spDate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dates));
        spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        AdapterView.OnItemSelectedListener filtreListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appliquerFiltres();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        spBudget.setOnItemSelectedListener(filtreListener);
        spDate.setOnItemSelectedListener(filtreListener);
        spType.setOnItemSelectedListener(filtreListener);

        //barre de recherche
        editTextSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filtrer(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        // selection voyage
        lvListeDestinations.setOnItemClickListener((parent, view, position, id) -> {
            Voyage voyage = listeVoyages.get(position);

            Intent intent = new Intent(this, Detail_destination.class);
            intent.putExtra("VOYAGE", voyage);
            startActivity(intent);
        });


        //dao, adapter
        dao = new ListeDestinationsDAO(this);
        chargerVoyagesDepuisServeur();


    }
    private void chargerVoyagesDepuisServeur() {
        dao.getVoyagesDepuisServeur("http://10.0.2.2:3000/voyages", voyages -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                listeVoyagesFiltrees = new ArrayList<>(voyages);
                listeVoyages = new ArrayList<>(voyages);

                adapter = new ListeDestinationsAdapter(Liste_destination.this, R.layout.liste_destinations_layout, listeVoyages);
                lvListeDestinations.setAdapter(adapter);

                appliquerFiltres();
            });
        });
    }

    private void appliquerFiltres() {
        String filtreBudget = spBudget.getSelectedItem().toString();
        String filtreDate = spDate.getSelectedItem().toString();
        String filtreType = spType.getSelectedItem().toString();

        listeVoyagesFiltrees.clear();

        for (Voyage v : listeVoyages) {
            boolean match = true;

            // Budget
            if (!filtreBudget.equals("Budget")) {
                int prix = (int) v.getPrix();
                if (filtreBudget.equals("0 - 200") && !(prix <= 200)) match = false;
                else if (filtreBudget.equals("201 - 400") && !(prix > 200 && prix <= 400)) match = false;
                else if (filtreBudget.equals("401 - 600") && !(prix > 400 && prix <= 600)) match = false;
                else if (filtreBudget.equals("601 - 800") && !(prix > 600 && prix <= 800)) match = false;
            }

            // Date (par mois)
            if (!filtreDate.equals("Date")) {
                boolean dateMatch = false;
                for (Trip t : v.getTrips()) {
                    if (t.getDate().startsWith(filtreDate)) {
                        dateMatch = true;
                        break;
                    }
                }
                if (!dateMatch) match = false;
            }

            // Type de voyage
            if (!filtreType.equals("Type") && !v.getType_de_voyage().equalsIgnoreCase(filtreType)) {
                match = false;
            }

            if (match) {
                listeVoyagesFiltrees.add(v);
            }
        }

        adapter.clear();
        adapter.addAll(listeVoyagesFiltrees);
        adapter.notifyDataSetChanged();
    }

}
