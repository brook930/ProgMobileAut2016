package com.nonamestudio.mobileproject;

/**
 * Created by melvi_000 on 09/12/2016.
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private ImageButton buttonPlay;

    private MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        backgroundMusic = MediaPlayer.create(this, R.raw.main_menu_theme);
        backgroundMusic.start();

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //adding a click listener
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

        //starting game activity
        startActivity(new Intent(this, GameActivity.class));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        backgroundMusic.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        backgroundMusic.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        backgroundMusic.stop();
        backgroundMusic.reset();
    }
}
