package com.example.ridenow;






import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;

import Authentication.LoginActivity;


public class SplashActivity extends AppCompatActivity {

    TextView title;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        title = findViewById(R.id.title);




        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        mediaPlayer.start();


        new Handler().postDelayed(() -> {
            mediaPlayer.release();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}




