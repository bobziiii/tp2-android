package com.example.comfelix_nath_anprojetx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Page_Connection extends AppCompatActivity {

    private EditText editTextEmail, editTextMdp;

    private Button btnSeConnecter;
    private TextView tvInscription;

    private final String url = "http://10.0.2.2:3000/clients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_connection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.editTextConnexionEmail);
        editTextMdp = findViewById(R.id.editTextConnexionMdp);
        btnSeConnecter = findViewById(R.id.btnSeConnecter);
        tvInscription = findViewById(R.id.tvInscription);

        btnSeConnecter.setOnClickListener(v -> authentifierUtilisateur());

        tvInscription.setOnClickListener(v -> {
            Intent intent = new Intent(this, Page_Inscription.class);
            startActivity(intent);
            finish();
        });
    }

    private void authentifierUtilisateur() {
        String email = editTextEmail.getText().toString().trim();
        String motDePasse = editTextMdp.getText().toString().trim();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // Affiche les détails de l'exception dans Logcat
                Log.e("Page_Connection", "Erreur lors de la connexion au serveur", e);
                runOnUiThread(() -> {
                    Toast.makeText(Page_Connection.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        // Analyser la réponse JSON
                        JSONArray utilisateurs = new JSONArray(response.body().string());

                        boolean utilisateurTrouve = false;
                        for (int i = 0; i < utilisateurs.length(); i++) {
                            JSONObject utilisateur = utilisateurs.getJSONObject(i);

                            // Vérifier si les identifiants correspondent
                            if (utilisateur.getString("email").equals(email) &&
                                    utilisateur.getString("mdp").equals(motDePasse)) {
                                utilisateurTrouve = true;
                                runOnUiThread(() -> {
                                    Toast.makeText(Page_Connection.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Page_Connection.this, Liste_destination.class);
                                    startActivity(intent);
                                    finish();
                                });
                                break;
                            }
                        }

                        if (!utilisateurTrouve) {
                            runOnUiThread(() -> Toast.makeText(Page_Connection.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show());
                        }

                    } catch (JSONException e) {
                        Log.e("Page_Connection", "Erreur de traitement des données JSON", e);
                        runOnUiThread(() -> Toast.makeText(Page_Connection.this, "Erreur de traitement des données", Toast.LENGTH_SHORT).show());                    }
                } else {
                    Log.e("Page_Connection", "Réponse du serveur non réussie, code HTTP : " + response.code());
                    runOnUiThread(() -> Toast.makeText(Page_Connection.this, "Échec de la récupération des données", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


}