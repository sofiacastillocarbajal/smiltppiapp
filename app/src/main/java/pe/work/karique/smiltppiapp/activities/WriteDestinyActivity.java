package pe.work.karique.smiltppiapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import pe.work.karique.smiltppiapp.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.libizo.CustomEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WriteDestinyActivity extends AppCompatActivity {

    private static final String TAG = "WriteDestinyActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;

    private SpeechRecognizer speechRecognizer;

    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    int paso = 0;
    AppCompatImageButton searchEffectAppCompatImageButton;
    AppCompatImageView locationAppCompatImageView;
    AppCompatImageView logoAppCompatImageView;
    TextView appHelpTextView;
    CustomEditText userVoiceInTextCustomEditText;
    AppCompatImageButton driverModeAppCompatImageButton;
    AppCompatImageView touchAppCompatImageView;
    RecognitionProgressView recognitionProgressView;
    AppCompatImageView speakerAppCompatImageView;

    TextToSpeech tts;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_destiny);
        hide();
        mContentView = findViewById(R.id.writeDestinyFullscreenConstraintLayout);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");

        touchAppCompatImageView = findViewById(R.id.touchAppCompatImageView);
        speakerAppCompatImageView = findViewById(R.id.speakerAppCompatImageView);
        driverModeAppCompatImageButton = findViewById(R.id.driverModeAppCompatImageButton);
        userVoiceInTextCustomEditText = findViewById(R.id.userVoiceInTextCustomEditText);
        appHelpTextView = findViewById(R.id.appHelpTextView);
        locationAppCompatImageView = findViewById(R.id.locationAppCompatImageView);
        logoAppCompatImageView = findViewById(R.id.logoAppCompatImageView);
        searchEffectAppCompatImageButton = findViewById(R.id.searchEffectAppCompatImageButton);
        driverModeAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(WriteDestinyActivity.this, DriverLoginActivity.class);
                WriteDestinyActivity.this.startActivity(mainIntent);
                finish();
            }
        });

        int[] colors = {
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        };

        int[] heights = {30, 34, 28, 33, 26};

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
        recognitionProgressView.setSpeechRecognizer(speechRecognizer);
        recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });
        recognitionProgressView.setColors(colors);
        recognitionProgressView.setBarMaxHeightsInDp(heights);
        recognitionProgressView.setCircleRadiusInDp(2);
        recognitionProgressView.setSpacingInDp(2);
        recognitionProgressView.setIdleStateAmplitudeInDp(2);
        recognitionProgressView.setRotationRadiusInDp(10);
        recognitionProgressView.play();

        searchEffectAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        tts = new TextToSpeech(WriteDestinyActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(WriteDestinyActivity.this, "El lenguaje de este dispositivo no está soportado", Toast.LENGTH_LONG).show();
                    } else {
                        tts.speak("¿A dónde se dirige ahora?", TextToSpeech.QUEUE_FLUSH, map);
                    }
                } else
                    Toast.makeText(WriteDestinyActivity.this, "La inicialización falló", Toast.LENGTH_LONG).show();
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                new Thread(){
                    public void run(){
                        WriteDestinyActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                speakerAppCompatImageView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onDone(String s) {
                new Thread(){
                    public void run(){
                        WriteDestinyActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                speakerAppCompatImageView.setVisibility(View.GONE);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }

    private void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-419");
        speechRecognizer.startListening(intent);
    }

    String text;

    private void showResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        text = "Contenido vacio";
        if (matches != null) {
            if (matches.size() > 0) {
                text = matches.get(0);
            }
        }
        tts.speak("Quizo decir " + text + ". Confirmar si o no.", TextToSpeech.QUEUE_FLUSH, map);

        recognitionProgressView.stop();
        //recognitionProgressView.play();

        searchEffectAppCompatImageButton.setVisibility(View.VISIBLE);
        touchAppCompatImageView.setVisibility(View.VISIBLE);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Se requieren permisos para grabar audios.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        }
    }

    private void search(){
        paso++;
        locationAppCompatImageView.setVisibility(View.GONE);
        logoAppCompatImageView.setVisibility(View.GONE);
        driverModeAppCompatImageButton.setVisibility(View.GONE);

        if (paso == 1){
            if (ContextCompat.checkSelfPermission(WriteDestinyActivity.this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                searchEffectAppCompatImageButton.setVisibility(View.GONE);
                touchAppCompatImageView.setVisibility(View.GONE);

                startRecognition();
                recognitionProgressView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecognition();
                    }
                }, 50);
            }
        }
        else if (paso == 2){
            appHelpTextView.setText(String.format("¿Quiso decir %s?\nConfirmar Si o No", text));
            userVoiceInTextCustomEditText.setText("Si ...");
        }
        else if (paso == 3){
            appHelpTextView.setText("¿En qué bus desea ir?\nBus 1,Bus 2");
            userVoiceInTextCustomEditText.setText("Bus 1 ...");
        }
        else if (paso == 4){
            appHelpTextView.setText("¿Quizo decir Bus 1?\nConfirmar Si o No");
            userVoiceInTextCustomEditText.setText("Si ...");
        }
        else if (paso == 5){
            appHelpTextView.setText("Se creo la solicitud de su viaje...\nEl bus se encuentra a 500 metros");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
        else if (paso == 6){
            appHelpTextView.setText("El bus se encuentra a 250 metros\nPrepárese para abordar");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
        else if (paso == 7){
            appHelpTextView.setText("El bus ha llegado al paradero.\nPor favor confirmar que está a bordo.");
            userVoiceInTextCustomEditText.setVisibility(View.VISIBLE);
            userVoiceInTextCustomEditText.setText("A bordo ...");
        }
        else if (paso == 8){
            appHelpTextView.setText("Usted está a bordo.");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
        else if (paso == 9){
            appHelpTextView.setText("El bus se encuentra a 500 metros de su paradero destino.");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
        else if (paso == 10){
            appHelpTextView.setText("El bus se encuentra a 250 metros de su paradero destino. Prepárese para descender.");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
        else if (paso == 11){
            appHelpTextView.setText("El bus ha llegado a su destino.\nPor favor confirmar que ha descendido del bus.");
            userVoiceInTextCustomEditText.setVisibility(View.VISIBLE);
            userVoiceInTextCustomEditText.setText("Finalizado ...");
        }
        else {
            appHelpTextView.setText("Usted ha finalizado su viaje.");
            userVoiceInTextCustomEditText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        hide();
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
}
