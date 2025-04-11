package com.example.comfelix_nath_anprojetx;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListeDestinationsDAO {

    private Context context;
    private static final String URL_VOYAGES = "http://10.0.2.2:3000/voyages";
    private final OkHttpClient client = new OkHttpClient();

    public ListeDestinationsDAO(Context context) {
        this.context = context;
    }

    public void getVoyagesDepuisServeur(String url, VoyageCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Voyage> voyages = new ArrayList<>();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            int id = obj.getInt("id");
                            int duree_jours = obj.getInt("duree_jours");
                            String nom = obj.getString("nom_voyage");
                            String desc = obj.getString("description");
                            String dest = obj.getString("destination");
                            String image = obj.getString("image_url");
                            String type = obj.getString("type_de_voyage");
                            String activites = obj.getString("activites_incluses");
                            double prix = obj.getDouble("prix");

                            List<Trip> trips = new ArrayList<>();
                            JSONArray tripArray = obj.getJSONArray("trips");
                            for (int j = 0; j < tripArray.length(); j++) {
                                JSONObject tripObj = tripArray.getJSONObject(j);
                                String date = tripObj.getString("date");
                                int nbPlaces = tripObj.getInt("nb_places_disponibles");
                                trips.add(new Trip(date, nbPlaces));
                            }

                            voyages.add(new Voyage(id, duree_jours, nom, desc, dest, image, type, activites, prix, trips));
                        }

                        // Renvoie les voyages à l’activité
                        callback.onVoyagesLoaded(voyages);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
