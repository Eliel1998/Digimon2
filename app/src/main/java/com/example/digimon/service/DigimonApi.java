package com.example.digimon.service;

import com.example.digimon.entity.Digimon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DigimonApi {
    @GET("digimon")
    Call<List<Digimon>> getDigimons();
}

