package pe.work.karique.smiltppiapp.activities;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import pe.work.karique.smiltppiapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class FullscreenActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 10;
    private final int SPLASH_DISPLAY_LENGTH = 2500;
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

    TextToSpeech tts;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.fullscreenConstraintLayout);
        hide();

        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts = new TextToSpeech(FullscreenActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(FullscreenActivity.this, "El lenguaje de este dispositivo no está soportado", Toast.LENGTH_LONG).show();
                    } else {
                        tts.speak("Bienvenidos a vamos upc", TextToSpeech.QUEUE_FLUSH, map);
                    }
                } else
                    Toast.makeText(FullscreenActivity.this, "La inicialización falló", Toast.LENGTH_LONG).show();
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                new Thread(){
                    public void run(){
                        FullscreenActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                //speakerAppCompatImageView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onDone(String s) {
                new Thread(){
                    public void run(){
                        FullscreenActivity.this.runOnUiThread(new Runnable(){
                            public void run(){
                                //speakerAppCompatImageView.setVisibility(View.GONE);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onError(String s) {

            }
        });

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(FullscreenActivity.this, WriteDestinyActivity.class);
                FullscreenActivity.this.startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        hide();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
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
