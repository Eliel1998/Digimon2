package com.example.digimon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digimon.controller.DigimonController;
import com.example.digimon.repository.DigimonRepository;
import com.example.digimon.service.DigimonApi;
import com.example.digimon.service.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button buttonSubmit;
    EditText editTextName;
    ListView listView;
    FloatingActionButton floatingActionButton;
    private static final String BASE_URL = "https://digimon-api.vercel.app/api/";
    DigimonController digimonController = new DigimonController();
    Map<String, String> letterImageUrlMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextName = findViewById(R.id.editTextName);
        listView = findViewById(R.id.listView);

        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        DigimonApi digimonApi = retrofit.create(DigimonApi.class);
        DigimonRepository.fetchDigimons(digimonApi);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                int characterCount = name.length();

                List<String> alphabetList = new ArrayList<>();
                for (char c = 'A'; c <= 'Z'; c++) {
                    alphabetList.add(String.valueOf(c));
                }

                List<String> selectedAlphabetList = alphabetList.subList(0, characterCount);
                letterImageUrlMap = digimonController.linkDigimonTheLetterOfTheAlphabet(selectedAlphabetList);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, selectedAlphabetList);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String imagem = letterImageUrlMap.get(item).toString();
                Intent intent = new Intent(MainActivity.this, DigimonActivity.class);
                intent.putExtra("image", imagem);
                String digimonName = digimonController.selectedDigimons.get(position).getName();
                System.out.println(digimonName + " <- nameeeee");
                intent.putExtra("userName", editTextName.getText().toString());
                intent.putExtra("digimonName", digimonName);
                startActivity(intent);
            }
        });

    }//end of onCreate
}