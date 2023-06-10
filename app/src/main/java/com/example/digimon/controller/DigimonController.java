package com.example.digimon.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.digimon.FirebaseApplication;
import com.example.digimon.entity.Digimon;
import com.example.digimon.repository.DigimonRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DigimonController {
    public static List<Digimon> selectedDigimons;
    public Map<String, String> linkDigimonTheLetterOfTheAlphabet(List<String> selectedAlphabetList) {

        Map<String, String> letterImageUrlMap = new HashMap<>();

        int digimonCount = DigimonRepository.getDigimonsFromJson().size();

        Random random = new Random();
        selectedDigimons = new ArrayList<>();

        for (String letter : selectedAlphabetList) {
            if (digimonCount > 0) {
                int randomIndex = random.nextInt(digimonCount);

                Digimon randomDigimon = DigimonRepository.getDigimonsFromJson().get(randomIndex);

                letterImageUrlMap.put(letter, randomDigimon.getImg());
                selectedDigimons.add(randomDigimon);

                DigimonRepository.getDigimonsFromJson().remove(randomIndex);

                digimonCount--;
            } else {
                letterImageUrlMap.put(letter, "https://digimon.shadowsmith.com/img/mekanorimon.jpg");
            }
        }


        for (Map.Entry<String, String> entry : letterImageUrlMap.entrySet()) {
            String letter = entry.getKey();
            String imageUrl = entry.getValue();
            Log.i(TAG, "vinculaDigimonALetraDoAlfabeto: Letter: " + letter + ", Image URL: " + imageUrl);
        }

        return letterImageUrlMap;

    }

    public void gravaDadosFirebase(String userName) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("scores");
        String key = databaseRef.push().getKey();

        String name = "Teste";
        int score = 100;

        // Cria um HashMap para armazenar os dados que serão gravados
        Map<String, Object> scoreMap = new HashMap<>();
        scoreMap.put("name", userName);
        scoreMap.put("score", score);

        // Grava os dados no Firebase
        databaseRef.child(key).setValue(scoreMap);

        System.out.println("Dados gravados com sucesso!");
    }
    public DatabaseReference getDatabaseReference() {
        FirebaseApp app = FirebaseApp.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(app);
        return firebaseDatabase.getReference();
    }

    public String incrementPontuation(String userName, int score) {
        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference.child("users").child(userName).child("score").setValue(score);
        return "Score incrementado com sucesso!";
    }
}
