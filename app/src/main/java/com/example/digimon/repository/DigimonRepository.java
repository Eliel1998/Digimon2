package com.example.digimon.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.digimon.entity.Digimon;
import com.example.digimon.service.DigimonApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DigimonRepository {

    public static List<Digimon> getDigimonsFromJson() {
        return digimonsFromJson;
    }

    public static void setDigimonsFromJson(List<Digimon> digimonsFromJson) {
        DigimonRepository.digimonsFromJson = digimonsFromJson;
    }

    static List<Digimon> digimonsFromJson;
    public static void fetchDigimons(DigimonApi digimonApi) {
        Call<List<Digimon>> call = digimonApi.getDigimons();
        call.enqueue(new Callback<List<Digimon>>() {
            @Override
            public void onResponse(Call<List<Digimon>> call, Response<List<Digimon>> response) {
                if (response.isSuccessful()) {
                    List<Digimon> digimons = response.body();
                    if (digimons != null) {

                        Gson gson = new Gson();
                        String json = gson.toJson(digimons);

                        digimonsFromJson = gson.fromJson(json, new TypeToken<List<Digimon>>() {
                        }.getType());

                        for (int i = 0; i < digimonsFromJson.size(); i++) {
                            System.out.println("Digimon: " + digimonsFromJson.get(i).getName());
                        }
                    } else {
                        Log.i(TAG, "Lista de digimons: " + "null");
                    }
                } else {
                    Log.i(TAG, "Erro: " + "deu erro aqui mano" + response);
                }
            }

            @Override
            public void onFailure(Call<List<Digimon>> call, Throwable t) {
                System.out.println("Erro: " + t.getMessage());
            }
        });
    }
}
