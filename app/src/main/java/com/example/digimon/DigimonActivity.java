package com.example.digimon;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.digimon.controller.DigimonController;
import com.example.digimon.entity.Digimon;
import com.example.digimon.service.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DigimonActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    ImageView imageViewDigimon;
    Button button;
    int vidas;
    int score;

    private TextToSpeech textToSpeech;
    DatabaseReference databaseReference;
    String userName="";

    private static final String CHANNEL_ID = "1";
    private static int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digimon);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        criarCanal();
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        String image = intent.getStringExtra("image");
        String digimonName = intent.getStringExtra("digimonName");

        List<Digimon> selectedDigimon = DigimonController.selectedDigimons;
        score = 0;
        vidas = 3;
        gerar();
        textToSpeech = new TextToSpeech(this, this);

        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        button = findViewById(R.id.btn);

        for (Digimon digimon : selectedDigimon) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(digimon.getName());
            radioGroup.addView(radioButton);
        }

        imageViewDigimon = findViewById(R.id.imgDigimon);
        new DownloadImageTask(imageViewDigimon).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, image);

        button.setOnClickListener(v -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(selectedId);
            String selectedDigimonName = radioButton.getText().toString();
            if (selectedDigimonName.equals(digimonName)) {
                score += 10;
                new DigimonController().incrementPontuation(userName, score, databaseReference);
                showCongratulationsDialog(score);
            } else {
                gerar();
                decreaseLives(icon1, icon2, icon3);
            }

        });

    }//onCreate

    private void decreaseLives(ImageView icon1,ImageView icon2,ImageView icon3) {
        vidas--;
        switch (vidas) {
            case 2:
                icon1.setVisibility(View.INVISIBLE);
                break;
            case 1:
                icon2.setVisibility(View.INVISIBLE);
                break;
            case 0:
                icon3.setVisibility(View.INVISIBLE);
                String mensagem = "Você perdeu todas as suas vidas!";
                speak(mensagem);
                break;
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Define o idioma de fala desejado.
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Idioma não suportado");
            } else {
                speak("Olá, boa sorte no jogo " + userName );
            }
        } else {
            Log.e("TTS", "Inicialização falhou");
        }
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void gerar() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Inicio do jogo")
                .setContentText("Voce tem 3 vidas e 10 pontos por acerto")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void criarCanal(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Inicio do jogo";
            String description = "Voce tem 3 vidas e 10 pontos por acerto";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void showCongratulationsDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parabéns, você acertou!")
                .setMessage("Você conquistou 10 pontos.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}