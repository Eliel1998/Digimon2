package com.example.digimon.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.digimon.entity.Digimon;
import com.example.digimon.entity.User;
import com.example.digimon.repository.DigimonRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

    public void incrementPontuation(String userName, int score, DatabaseReference databaseReference) {
        databaseReference.child("users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot scoreSnapshot = dataSnapshot.child("score");
                    if (scoreSnapshot.exists()) {
                        Long currentScore = scoreSnapshot.getValue(Long.class);

                        if (currentScore != null) {
                            currentScore += score;
                            updateAndAddUser(currentScore.intValue(), userName, databaseReference);
                        } else {
                            Log.e("Firebase", "O score do usuário é inválido ou não existe.");
                        }
                    } else {
                        Log.e("Firebase", "O score do usuário não existe.");
                    }
                } else {
                    Log.e("Firebase", "Usuário não encontrado.");
                    updateAndAddUser(score, userName, databaseReference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Erro na busca do usuário: " + databaseError.getMessage());
            }
        });
    }

    public void updateAndAddUser(int score, String userName, DatabaseReference databaseReference) {
        databaseReference.child("users").child(userName).child("score").setValue(score, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("Firebase", "Erro ao salvar o valor: " + databaseError.getMessage());
                } else {
                    Log.d("Firebase", "Valor salvo com sucesso!");
                }
            }
        });
    }

    public void getUsers(DatabaseReference databaseReference) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey();
                    Long score = userSnapshot.child("score").getValue(Long.class);
                    User user = new User(userName, score);
                    userList.add(user);
                }

                for (User user : userList) {
                    Log.d("Firebase", "Usuário: " + user.getUserName() + ", Score: " + user.getScore());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ocorreu um erro ao obter os dados do banco de dados
                Log.e("Firebase", "Erro ao obter os dados: " + databaseError.getMessage());
            }
        });

    }
}
