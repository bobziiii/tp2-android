package com.example.comfelix_nath_anprojetx;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Detail_destination extends AppCompatActivity {

    private Voyage voyage;
    private TextView tvVilleProvince, tvDescription, tvDuree, tvActivites, tvPrix, tvNbPlaceDemande, tvNbPlaceDispo;
    private Button btnReserve;
    private ImageButton imgBtnMoins, imgBtnPlus, btnRetour;
    private Spinner spChoixDate;
    private ImageView imgDestination;

    private int nbPlacesDispoActuel = 0;
    private final int[] nbDemande = {1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_destination);

        tvVilleProvince = findViewById(R.id.tvDestination);
        tvDescription = findViewById(R.id.tvDescription);
        tvDuree = findViewById(R.id.tvDuree);
        tvActivites = findViewById(R.id.tvListeActivite);
        tvPrix = findViewById(R.id.tvPrix);
        tvNbPlaceDemande = findViewById(R.id.tvNbPlaceDemande);
        tvNbPlaceDispo = findViewById(R.id.tvNbPlaceDispo);
        spChoixDate = findViewById(R.id.spChoixDate);
        imgDestination = findViewById(R.id.imgDestination);
        imgBtnMoins = findViewById(R.id.imgBtnMoins);
        imgBtnPlus = findViewById(R.id.imgBtnPlus);
        btnReserve = findViewById(R.id.btnReserve);
        btnRetour = findViewById(R.id.btnRetour);

        btnRetour.setOnClickListener(v -> finish());

        voyage = (Voyage) getIntent().getSerializableExtra("VOYAGE");

        if (voyage != null) {
            tvVilleProvince.setText(voyage.getDestination());
            tvDescription.setText(voyage.getDescription());
            tvDuree.setText("Durée : " + voyage.getDuree()+ " jours");
            tvActivites.setText(voyage.getActivites_incluses());
            tvPrix.setText("Prix : " + voyage.getPrix() + "$");

            // Charger image
            new Thread(() -> {
                try {
                    URL url = new URL(voyage.getImage_url());
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(() -> imgDestination.setImageBitmap(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Remplir Spinner
            List<String> dates = new ArrayList<>();
            for (Trip trip : voyage.getTrips()) {
                dates.add(trip.getDate());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spChoixDate.setAdapter(adapter);

            // Initialiser avec la première date
            if (!voyage.getTrips().isEmpty()) {
                nbPlacesDispoActuel = voyage.getTrips().get(0).getNb_places_disponibles();
                tvNbPlaceDispo.setText("Places disponibles : " + nbPlacesDispoActuel);
                btnReserve.setEnabled(nbPlacesDispoActuel > 0);
            }

            // Choix de date
            spChoixDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    nbPlacesDispoActuel = voyage.getTrips().get(position).getNb_places_disponibles();
                    tvNbPlaceDispo.setText("Places disponibles : " + nbPlacesDispoActuel);
                    nbDemande[0] = 1;
                    tvNbPlaceDemande.setText(String.valueOf(nbDemande[0]));
                    btnReserve.setEnabled(nbPlacesDispoActuel > 0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        // Achat
        tvNbPlaceDemande.setText(String.valueOf(nbDemande[0]));

        imgBtnMoins.setOnClickListener(v -> {
            if (nbDemande[0] > 1) {
                nbDemande[0]--;
                tvNbPlaceDemande.setText(String.valueOf(nbDemande[0]));
            }
        });

        imgBtnPlus.setOnClickListener(v -> {
            if (nbDemande[0] < nbPlacesDispoActuel) {
                nbDemande[0]++;
                tvNbPlaceDemande.setText(String.valueOf(nbDemande[0]));
            }
        });

        btnReserve.setOnClickListener(v -> {
            int nbDemandeFinal = nbDemande[0];

            if (nbPlacesDispoActuel == 0) {
                btnReserve.setEnabled(false);
                return;
            }

            if (nbDemandeFinal > nbPlacesDispoActuel) {
                new AlertDialog.Builder(this)
                        .setTitle("Réservation impossible")
                        .setMessage("Seulement " + nbPlacesDispoActuel + " places disponibles.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            double prixParPersonne = voyage.getPrix();
            double total = prixParPersonne * nbDemandeFinal;

            new AlertDialog.Builder(this)
                    .setTitle("Confirmer la réservation")
                    .setMessage("Total: " + total + "$ pour " + nbDemandeFinal + " personne(s). Confirmer ?")
                    .setPositiveButton("Confirmer", (dialog, which) -> {
                        int nouvellesPlaces = nbPlacesDispoActuel - nbDemandeFinal;
                        nbPlacesDispoActuel = nouvellesPlaces;
                        tvNbPlaceDispo.setText("Places disponibles : " + nbPlacesDispoActuel);
                        mettreAJourPlacesDisponibles(voyage.getId(), spChoixDate.getSelectedItemPosition(), nouvellesPlaces);
                        Toast.makeText(this, "Réservation confirmée !", Toast.LENGTH_LONG).show();

                        if (nouvellesPlaces == 0) {
                            btnReserve.setEnabled(false);
                        }

                        finish();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    private void mettreAJourPlacesDisponibles(int voyageId, int tripIndex, int nouvellesPlaces) {
        String url = "http://10.0.2.2:3000/voyages";
        OkHttpClient client = new OkHttpClient();

        Request getRequest = new Request.Builder().url(url).build();

        client.newCall(getRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Detail_destination.this, "Erreur de connexion", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    Log.d("JSON Response", responseData);  // Affiche la réponse JSON brute

                    try {
                        JSONArray voyagesArray = new JSONArray(responseData);

                        for (int i = 0; i < voyagesArray.length(); i++) {
                            JSONObject voyageJSON = voyagesArray.getJSONObject(i);

                            if (voyageJSON.getInt("id") == voyageId) {
                                JSONArray tripsArray = voyageJSON.getJSONArray("trips");

                                if (tripIndex >= 0 && tripIndex < tripsArray.length()) {
                                    JSONObject tripToUpdate = tripsArray.getJSONObject(tripIndex);
                                    tripToUpdate.put("nb_places_disponibles", nouvellesPlaces);

                                    RequestBody body = RequestBody.create(voyageJSON.toString(), MediaType.parse("application/json"));
                                    Request putRequest = new Request.Builder()
                                            .url("http://10.0.2.2:3000/voyages/" + voyageId)
                                            .put(body)
                                            .build();

                                    client.newCall(putRequest).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            runOnUiThread(() -> Toast.makeText(Detail_destination.this, "Échec de mise à jour", Toast.LENGTH_SHORT).show());
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            runOnUiThread(() -> {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(Detail_destination.this, "Places mises à jour", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Detail_destination.this, "Erreur serveur", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(Detail_destination.this, "Erreur JSON", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }
}
