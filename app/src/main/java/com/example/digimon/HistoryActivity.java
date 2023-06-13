package com.example.digimon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.digimon.controller.DigimonController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseReference;
    DigimonController digimonController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = findViewById(R.id.listViewRanking);
        digimonController = new DigimonController();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        digimonController.getUsers(databaseReference);
    }
}