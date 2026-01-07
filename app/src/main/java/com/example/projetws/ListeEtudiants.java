package com.example.projetws;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.adapter.EtudiantAdapter;
import com.example.projetws.beans.Etudiant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeEtudiants extends AppCompatActivity {

    RecyclerView rvEtudiants;
    EtudiantAdapter adapter;
    List<Etudiant> etudiants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        rvEtudiants = findViewById(R.id.rvEtudiants);
        rvEtudiants.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EtudiantAdapter(etudiants, this, this::showOptions);
        rvEtudiants.setAdapter(adapter);

        loadEtudiants();
    }

    private void loadEtudiants() {
        String url = "http://192.168.0.184/project/ws/loadEtudiant.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        etudiants.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            etudiants.add(new Etudiant(
                                    obj.getInt("id"),
                                    obj.getString("nom"),
                                    obj.getString("prenom"),
                                    obj.getString("ville"),
                                    obj.getString("sexe")
                            ));
                        }
                        adapter.updateList(etudiants);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Erreur : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showOptions(Etudiant etudiant) {
        // Popup pour modification / suppression
        String[] options = {"Modifier", "Supprimer"};
        new AlertDialog.Builder(this)
                .setTitle(etudiant.getNom() + " " + etudiant.getPrenom())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) editEtudiant(etudiant);
                    else confirmDelete(etudiant);
                }).show();
    }

    private void editEtudiant(Etudiant e) {
        // Lancer une activité ou Dialog pour modifier les données
        // Puis appeler le Web Service de modification
    }

    private void confirmDelete(Etudiant e) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous supprimer cet étudiant ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteEtudiant(e))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteEtudiant(Etudiant e) {
        String url = "http://192.168.100.9/project/ws/deleteEtudiant.php?id=" + e.getId();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> loadEtudiants(), // recharger la liste
                error -> Toast.makeText(this, "Erreur : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
