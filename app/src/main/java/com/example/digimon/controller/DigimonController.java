package com.example.digimon.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.digimon.entity.Digimon;
import com.example.digimon.repository.DigimonRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DigimonController {
    public void linkDigimonTheLetterOfTheAlphabet(List<String> selectedAlphabetList) {

        Map<String, String> letterImageUrlMap = new HashMap<>();

        int digimonCount = DigimonRepository.getDigimonsFromJson().size();

        Random random = new Random();

        for (String letter : selectedAlphabetList) {
            if (digimonCount > 0) {
                int randomIndex = random.nextInt(digimonCount);

                Digimon randomDigimon = DigimonRepository.getDigimonsFromJson().get(randomIndex);

                letterImageUrlMap.put(letter, randomDigimon.getImg());

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

    }
}
