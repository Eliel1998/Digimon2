package com.example.digimon;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.List;
import java.util.Locale;

public class DigimonActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    ImageView imageViewDigimon;
    Button button;
    int vidas;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digimon);

        textToSpeech = new TextToSpeech(this, this);


        vidas = 3;
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);


        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        button = findViewById(R.id.btn);


        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        List<Digimon> selectedDigimon = DigimonController.selectedDigimons;
        String digimonName = intent.getStringExtra("digimonName");

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
                Log.d(TAG, "onCreate: " + "Correct");
            } else {
                Log.d(TAG, "onCreate: " + "Wrong");
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

        });

    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Definir o idioma de fala desejado (opcional)
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Idioma não suportado");
            } else {
                // O TextToSpeech está pronto para ser utilizado
                speak("Olá, bem-vindo ao TextToSpeech!");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libere recursos do TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}