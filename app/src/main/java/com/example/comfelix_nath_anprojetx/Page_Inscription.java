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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Page_Inscription extends AppCompatActivity {

    private EditText editTextNom, editTextPrenom, editTextEmail, editTextPhone, editTextAge, editTextAdresse, editTextMdp;
    private Button btnInscription;
    private TextView tvSeConnecter;

    private final OkHttpClient client = new OkHttpClient();
    private final String url = "http://10.0.2.2:3000/clients";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_inscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextMdp = findViewById(R.id.editTextMdp);
        btnInscription = findViewById(R.id.btnInscription);
        tvSeConnecter = findViewById(R.id.tvSeConnecter);

        btnInscription.setOnClickListener(v -> inscrireUtilisateur());

        tvSeConnecter.setOnClickListener(v -> {
            Intent intent = new Intent(this, Page_Connection.class);
            startActivity(intent);
            finish();
        });

    }

    private void inscrireUtilisateur() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(Page_Inscription.this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Recherche de l'ID maximum
                        int maxId = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(user.getString("id"));
                            if (id > maxId) {
                                maxId = id;
                            }
                        }

                        int newId = maxId + 1;

                        JSONObject newUser = new JSONObject();
                        newUser.put("id", String.valueOf(newId));
                        newUser.put("nom", editTextNom.getText().toString());
                        newUser.put("prenom", editTextPrenom.getText().toString());
                        newUser.put("email", editTextEmail.getText().toString());
                        newUser.put("mdp", editTextMdp.getText().toString());
                        newUser.put("age", Integer.parseInt(editTextAge.getText().toString()));
                        newUser.put("telephone", editTextPhone.getText().toString());
                        newUser.put("adresse", editTextAdresse.getText().toString());

                        RequestBody body = RequestBody.create(newUser.toString(), JSON);
                        Request postRequest = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();

                        client.newCall(postRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(() ->
                                        Toast.makeText(Page_Inscription.this, "Erreur d'envoi", Toast.LENGTH_SHORT).show()
                                );
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                runOnUiThread(() -> {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(Page_Inscription.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Page_Inscription.this, Liste_destination.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Page_Inscription.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    } catch (JSONException e) {
                        runOnUiThread(() ->
                                Toast.makeText(Page_Inscription.this, "Erreur JSON", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            }
        });
    }
}
