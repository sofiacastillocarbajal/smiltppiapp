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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.ProgressBar;
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
    ProgressBar circleProgressBar;
    AppCompatImageView carAppCompatImageView;

    String text = "";
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
        circleProgressBar = findViewById(R.id.circleProgressBar);
        circleProgressBar.setVisibility(View.GONE);
        carAppCompatImageView = findViewById(R.id.carAppCompatImageView);
        carAppCompatImageView.setVisibility(View.GONE);

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
                vibrate(100);
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
                //search();
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
                        TRAVEL_STATE = TRAVEL_STATE_A_DONDE_SE_DIRIGE;
                        tts.speak(A_DONDE_SE_DIRIGE_AHORA, TextToSpeech.QUEUE_FLUSH, map);
                        waitAndStartRecognition(2);
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

    Intent intent = null;
    private void startRecognition() {
        vibrate(100);
        if (ContextCompat.checkSelfPermission(WriteDestinyActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            recognitionProgressView.play();
            searchEffectAppCompatImageButton.setVisibility(View.GONE);
            touchAppCompatImageView.setVisibility(View.GONE);

            if (intent == null) {
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-419");
            }
            speechRecognizer.startListening(intent);
        }
    }

    //mensajes
    private String CONTENIDO_VACIO = "Contenido vacio";
    private String COMENCEMOS = "Comencemos...";
    private String NO_SE_ENTENDIO_EL_MENSAJE_REPETIRLO = "No se entendió el mensaje, porfavor, vuelva a repetirlo";
    private String A_DONDE_SE_DIRIGE_AHORA = "¿A dónde se dirige ahora?";
    private String SE_CREO_SOLICITUD_DE_VIAJE_743_M = "Se creó la solicitud de su viaje. El bus UPC con ruta 1 se encuentra a 743 metros";
    private String BUS_A_500_M = "El bus se encuentra a 500 metros";
    private String BUS_A_250_M = "El bus se encuentra a 250 metros. Prepárese para abordar.";
    private String BUS_LLEGO_A_PARADERO_DE_USER = "El bus ha llegado al paradero. Por favor confirmar que está a bordo.";
    private String USTED_ESTA_A_BORDO = "Usted está a bordo.";
    private String USTED_A_FINALIZADO_SU_VIAJE = "Usted ha finalizado su viaje.";
    //
    private String BUS_LLEGO_A_500_M_DE_PRIMAVERA = "El bus se encuentra a 500 metros del puente primavera.";
    private String BUS_LLEGO_A_250_M_DE_PRIMAVERA = "El bus se encuentra a 250 metros del puente primavera. Prepárese si va a descender.";
    private String BUS_LLEGO_A_PRIMAVERA = "El bus ha llegado al puente primavera.";
    //
    private String BUS_LLEGO_A_500_M_DE_ENCALADA_CON_PRIMAVERA = "El bus se encuentra a 500 metros de la intersección avenida encalada con avenida primavera.";
    private String BUS_LLEGO_A_250_M_DE_ENCALADA_CON_PRIMAVERA = "El bus se encuentra a 250 metros de la intersección avenida encalada con avenida primavera. Prepárese si va a descender.";
    private String BUS_LLEGO_A_ENCALADA_CON_PRIMAVERA = "El bus ha llegado a la intersección avenida encalada con avenida primavera.";
    //
    private String BUS_LLEGO_A_500_M_DE_UPC = "El bus se encuentra a 500 metros de la UPC Monterrico.";
    private String BUS_LLEGO_A_250_M_DE_UPC = "El bus se encuentra a 250 metros de la UPC Monterrico. Prepárese para descender.";
    private String BUS_LLEGO_A_UPC = "El bus ha llegado a la UPC Monterrico.";
    private String BUS_CONFIRME_ESTADO = " Por favor confirmar su estado";

    //respuestas
    private String OK_BUS_UPC_MONTERRICO = "Ok bus upc monterrico";
    private String OK_BUS_SI = "Ok bus si";
    private String OK_BUS_NO = "Ok bus no";
    private String OK_BUS_A_BORDO = "Ok bus a bordo";
    private String OK_BUS_EN_ESPERA = "Ok bus en espera";
    private String OK_BUS_OTRA_PARADA = "Ok bus otra parada";
    private String OK_BUS_FINALIZADO = "Ok bus finalizado";

    //estados de viaje
    private float TRAVEL_STATE_A_DONDE_SE_DIRIGE = 3;
    private float TRAVEL_STATE_A_DONDE_SE_DIRIGE_CONFIRMAR_SI_O_NO = 4;
    private float TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_743_M = 5;
    private float TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_500_M = 6;
    private float TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_250_M = 7;
    private float TRAVEL_STATE_BUS_LLEGO_A_PARADERO_DE_USER = 8;
    private float TRAVEL_STATE_USTED_ESTA_A_BORDO = 9;
    private float TRAVEL_STATE_FINALIZADO = 16;
    //
    private float TRAVEL_STATE_A_500_M_DE_PRIMAVERA = 10;
    private float TRAVEL_STATE_A_250_M_DE_PRIMAVERA = 11;
    private float TRAVEL_STATE_LLEGO_A_PRIMAVERA = 12;
    //
    private float TRAVEL_STATE_A_500_M_DE_ENCALADA_CON_PRIMAVERA = 13;
    private float TRAVEL_STATE_A_250_M_DE_ENCALADA_CON_PRIMAVERA = 14;
    private float TRAVEL_STATE_LLEGO_A_ENCALADA_CON_PRIMAVERA = 15;
    //
    private float TRAVEL_STATE_A_500_M_DE_UPC = 17;
    private float TRAVEL_STATE_A_250_M_DE_UPC = 18;
    private float TRAVEL_STATE_LLEGO_A_UPC = 19;


    private float TRAVEL_STATE = TRAVEL_STATE_A_DONDE_SE_DIRIGE;

    private void showResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        text = CONTENIDO_VACIO;
        if (matches != null) {
            if (matches.size() > 0) {
                text = matches.get(0);
            }
        }
        alterRules();

        if (TRAVEL_STATE == TRAVEL_STATE_A_DONDE_SE_DIRIGE) {
            if (text.trim().equals(OK_BUS_UPC_MONTERRICO)) {
                tts.speak("Quizo decir " + text + ". Confirmar si o no.", TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setText(text);

                TRAVEL_STATE = TRAVEL_STATE_A_DONDE_SE_DIRIGE_CONFIRMAR_SI_O_NO;
                waitAndStartRecognition(5);
            }
            else failInTextRecognition(7, true, text);
        }
        else if (TRAVEL_STATE == TRAVEL_STATE_A_DONDE_SE_DIRIGE_CONFIRMAR_SI_O_NO) {
            if (text.equals(OK_BUS_SI)) {
                hideSuperiorLogos();
                tts.speak(SE_CREO_SOLICITUD_DE_VIAJE_743_M, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(SE_CREO_SOLICITUD_DE_VIAJE_743_M);

                TRAVEL_STATE = TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_743_M;
                waitAndShowSeEncuentra500m(15);
            }
            else if (text.equals(OK_BUS_NO)) {
                showSuperiorLogos();
                userVoiceInTextCustomEditText.setVisibility(View.VISIBLE);
                userVoiceInTextCustomEditText.setText(COMENCEMOS);
                appHelpTextView.setText(A_DONDE_SE_DIRIGE_AHORA);

                TRAVEL_STATE = TRAVEL_STATE_A_DONDE_SE_DIRIGE;
                waitAndStartRecognition(5);
            }
            else failInTextRecognition(5, false, text);
        }
        else if (TRAVEL_STATE == TRAVEL_STATE_BUS_LLEGO_A_PARADERO_DE_USER) {
            if (text.equals(OK_BUS_A_BORDO)) {
                hideSuperiorLogos();
                tts.speak(USTED_ESTA_A_BORDO, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(USTED_ESTA_A_BORDO);

                TRAVEL_STATE = TRAVEL_STATE_USTED_ESTA_A_BORDO;
                waitAndShowBusA500MDePrimavera(15);
            }
            else if (text.equals(OK_BUS_EN_ESPERA)) {
                hideSuperiorLogos();
                tts.speak(SE_CREO_SOLICITUD_DE_VIAJE_743_M, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(SE_CREO_SOLICITUD_DE_VIAJE_743_M);

                TRAVEL_STATE = TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_743_M;
                waitAndShowSeEncuentra500m(15);
            }
            else if (text.equals(OK_BUS_OTRA_PARADA)) {
                TRAVEL_STATE = TRAVEL_STATE_A_DONDE_SE_DIRIGE;
                tts.speak(A_DONDE_SE_DIRIGE_AHORA, TextToSpeech.QUEUE_FLUSH, map);
                waitAndStartRecognition(5);
            }
            else failInTextRecognition(5, false, text);
        }
        else if (TRAVEL_STATE == TRAVEL_STATE_LLEGO_A_PRIMAVERA) {
            if (text.equals(OK_BUS_FINALIZADO)) {
                hideSuperiorLogos();
                tts.speak(USTED_A_FINALIZADO_SU_VIAJE, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(USTED_A_FINALIZADO_SU_VIAJE);

                TRAVEL_STATE = TRAVEL_STATE_FINALIZADO;
            }
            else if (text.equals(OK_BUS_A_BORDO)) { //para que continúe con el viaje
                hideSuperiorLogos();
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                waitAndShowBusA500MDeEncaladaConPrimavera(10);
            }
            else failInTextRecognition(5, false, text);
        }
        else if (TRAVEL_STATE == TRAVEL_STATE_LLEGO_A_ENCALADA_CON_PRIMAVERA) {
            if (text.equals(OK_BUS_FINALIZADO)) {
                hideSuperiorLogos();
                tts.speak(USTED_A_FINALIZADO_SU_VIAJE, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(USTED_A_FINALIZADO_SU_VIAJE);

                TRAVEL_STATE = TRAVEL_STATE_FINALIZADO;
            }
            else if (text.equals(OK_BUS_A_BORDO)) { //para que continúe con el viaje
                hideSuperiorLogos();
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                waitAndShowBusA500MDeUPC(10);
            }
            else failInTextRecognition(5, false, text);
        }
        else if (TRAVEL_STATE == TRAVEL_STATE_LLEGO_A_UPC) {
            if (text.equals(OK_BUS_FINALIZADO)) {
                hideSuperiorLogos();
                tts.speak(USTED_A_FINALIZADO_SU_VIAJE, TextToSpeech.QUEUE_FLUSH, map);
                recognitionProgressView.stop();
                userVoiceInTextCustomEditText.setVisibility(View.GONE);
                appHelpTextView.setText(USTED_A_FINALIZADO_SU_VIAJE);

                waitAndFinish(3);
            }
            else failInTextRecognition(5, false, text);
        }

        searchEffectAppCompatImageButton.setVisibility(View.VISIBLE);
    }

    private void failInTextRecognition(int seconds, boolean dh, String mensaje){
        tts.speak(NO_SE_ENTENDIO_EL_MENSAJE_REPETIRLO + (dh ? A_DONDE_SE_DIRIGE_AHORA : ""), TextToSpeech.QUEUE_FLUSH, map);
        waitAndStartRecognition(seconds);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void alterRules(){
        text = text.replace("ups", "upc");
        text = text.replace("UPS", "upc");
        text = text.replace("Ups", "upc");

        text = text.replace("Monterrey", "monterrico");
        text = text.replace("monterrey", "monterrico");

        text = text.replace(" vs ", " upc ");

        text = text.replace("google", "bus");
        text = text.replace("Google", "bus");
        text = text.replace("GOOGLE", "bus");

        text = text.replace("okay", "Ok");
        text = text.replace("Okay", "Ok");
        text = text.replace("OK", "Ok");
        text = text.replace("ok", "Ok");

        text = text.replace("vos", "bus");
        text = text.replace("pues", "bus");

        text = text.replace("Sí", "si");
        text = text.replace("sí", "si");

        text = text.replace("abordo", "a bordo");
    }

    private void waitAndStartRecognition(int seconds){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startRecognition();
            }
        }, seconds * 1000);
    }

    private void carWaitEffectV(){
        circleProgressBar.setVisibility(View.VISIBLE);
        carAppCompatImageView.setVisibility(View.VISIBLE);
        touchAppCompatImageView.setVisibility(View.GONE);
    }

    private void carWaitEffectG(){
        circleProgressBar.setVisibility(View.GONE);
        carAppCompatImageView.setVisibility(View.GONE);
        touchAppCompatImageView.setVisibility(View.VISIBLE);
    }

    private void waitAndShowSeEncuentra500m(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_A_500_M, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_A_500_M);

                TRAVEL_STATE = TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_500_M;
                waitAndShowSeEncuentra250m(15);
            }
        }, seconds * 1000);
    }

    private void waitAndShowSeEncuentra250m(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_A_250_M, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_A_250_M);

                TRAVEL_STATE = TRAVEL_STATE_SE_CREO_SOLICITUD_DE_VIAJE_250_M;
                waitAndShowBusLlegoAParederoDeUser(15);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusLlegoAParederoDeUser(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_PARADERO_DE_USER, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_PARADERO_DE_USER);

                TRAVEL_STATE = TRAVEL_STATE_BUS_LLEGO_A_PARADERO_DE_USER;
                waitAndStartRecognition(6);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA500MDePrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_500_M_DE_PRIMAVERA, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_500_M_DE_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_A_500_M_DE_PRIMAVERA;
                waitAndShowBusA250MDePrimavera(15);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA250MDePrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_250_M_DE_PRIMAVERA, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_250_M_DE_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_A_250_M_DE_PRIMAVERA;
                waitAndShowBusLlegoAPrimavera(15);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusLlegoAPrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_PRIMAVERA + BUS_CONFIRME_ESTADO, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_LLEGO_A_PRIMAVERA;
                waitAndStartRecognition(5);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA500MDeEncaladaConPrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_500_M_DE_ENCALADA_CON_PRIMAVERA, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_500_M_DE_ENCALADA_CON_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_A_500_M_DE_ENCALADA_CON_PRIMAVERA;
                waitAndShowBusA250MDeEncaladaConPrimavera(25);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA250MDeEncaladaConPrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_250_M_DE_ENCALADA_CON_PRIMAVERA, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_250_M_DE_ENCALADA_CON_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_A_250_M_DE_ENCALADA_CON_PRIMAVERA;
                waitAndShowBusLlegoAEncaladaConPrimavera(30);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusLlegoAEncaladaConPrimavera(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_ENCALADA_CON_PRIMAVERA + BUS_CONFIRME_ESTADO, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_ENCALADA_CON_PRIMAVERA);

                TRAVEL_STATE = TRAVEL_STATE_LLEGO_A_ENCALADA_CON_PRIMAVERA;
                waitAndStartRecognition(10);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA500MDeUPC(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_500_M_DE_UPC, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_500_M_DE_UPC);

                TRAVEL_STATE = TRAVEL_STATE_A_500_M_DE_UPC;
                waitAndShowBusA250MDeUPC(25);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusA250MDeUPC(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_250_M_DE_UPC, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_250_M_DE_UPC);

                TRAVEL_STATE = TRAVEL_STATE_A_250_M_DE_UPC;
                waitAndShowBusLlegoAUPC(30);
            }
        }, seconds * 1000);
    }

    private void waitAndShowBusLlegoAUPC(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                tts.speak(BUS_LLEGO_A_UPC + BUS_CONFIRME_ESTADO, TextToSpeech.QUEUE_FLUSH, map);
                appHelpTextView.setText(BUS_LLEGO_A_UPC);

                TRAVEL_STATE = TRAVEL_STATE_LLEGO_A_UPC;
                waitAndStartRecognition(7);
            }
        }, seconds * 1000);
    }

    private void waitAndFinish(int seconds){
        carWaitEffectV();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                carWaitEffectG();
                finish();
            }
        }, seconds * 1000);
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

    private void hideSuperiorLogos() {
        changeSuperiorLogosVisibility(View.GONE);
    }

    private void showSuperiorLogos() {
        changeSuperiorLogosVisibility(View.VISIBLE);
    }

    private void changeSuperiorLogosVisibility(int visibility){
        locationAppCompatImageView.setVisibility(visibility);
        logoAppCompatImageView.setVisibility(visibility);
        driverModeAppCompatImageButton.setVisibility(visibility);
    }

    private void vibrate(int timeMiliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (v != null) {
                v.vibrate(VibrationEffect.createOneShot(timeMiliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            //deprecated in API 26
            if (v != null) {
                v.vibrate(timeMiliseconds);
            }
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
