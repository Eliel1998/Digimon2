package com.example.digimon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.digimon.controller.DigimonController;
import com.example.digimon.repository.DigimonRepository;
import com.example.digimon.service.DigimonApi;
import com.example.digimon.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button buttonSubmit;
    EditText editTextName;
    ListView listView;
    TextView textViewCharacterCount;
    private static final String BASE_URL = "https://digimon-api.vercel.app/api/";
    DigimonController digimonController = new DigimonController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                digimonController.linkDigimonTheLetterOfTheAlphabet(selectedAlphabetList);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, selectedAlphabetList);
                listView.setAdapter(adapter);
            }
        });

    }//end of onCreate



}