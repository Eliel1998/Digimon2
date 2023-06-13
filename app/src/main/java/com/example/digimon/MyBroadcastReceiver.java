package com.example.digimon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Voce pode verificar o Ranking no botão flutuante na pagina inicial", Toast.LENGTH_SHORT).show();
    }
}
