package com.example.digimon.entity;

import com.google.gson.annotations.SerializedName;

public class Digimon {
    @SerializedName("name")
    private String name;

    @SerializedName("img")
    private String img;

    @SerializedName("level")
    private String level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
