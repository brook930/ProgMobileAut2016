package com.nonamestudio.mobileproject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import Game.Constants;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sManager;
    private long timeSinceLastDodge;

    private ViewInGame view;

    private boolean isGamePaused = false;

    private MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backgroundMusic = MediaPlayer.create(this, R.raw.main_menu_theme);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        view = new ViewInGame(this, m_handler);
        setContentView(view);

        timeSinceLastDodge = SystemClock.uptimeMillis();

    }
    public void onClick(View v) {
        showPauseMenu();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        //else it will output the Roll, Pitch and Yawn values
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {

            //Si la vitesse de rotation autour de Z est superieur a 90 et que l'esquive n'est pas sous cooldown alors esquive a gauche
            if((event.values[2] * (float)(180/Math.PI)) >= 180.0f && SystemClock.uptimeMillis() - timeSinceLastDodge >= 300.0f) {
                Log.i("pouet", "ESQUIVE GAUCHE");
                timeSinceLastDodge = SystemClock.uptimeMillis();
                view.input = ViewInGame.Input.LEFTZROT;
            }
            else if((event.values[2] * (float)(180/Math.PI)) <= -180.0f && SystemClock.uptimeMillis() - timeSinceLastDodge >= 300.0f) {
                Log.i("pouet", "ESQUIVE DROITE");
                timeSinceLastDodge = SystemClock.uptimeMillis();
                view.input = ViewInGame.Input.RIGHTZROT;
            }
            //FRAPPES GAUCHE ET DROITE
            if((event.values[0] * (float)(180/Math.PI)) >= 180.0f && SystemClock.uptimeMillis() - timeSinceLastDodge >= 300.0f) {
                Log.i("pouet", "FRAPPE DROITE");
                timeSinceLastDodge = SystemClock.uptimeMillis();
                view.input = ViewInGame.Input.RIGHTYROT;
            }
            else if((event.values[0] * (float)(180/Math.PI)) <= -180.0f && SystemClock.uptimeMillis() - timeSinceLastDodge >= 300.0f) {
                Log.i("pouet", "FRAPPE GAUCHE");
                timeSinceLastDodge = SystemClock.uptimeMillis();
                view.input = ViewInGame.Input.LEFTYROT;
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    //Stop listening to the accelerometer when user is out of app
    @Override
    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(this);

        Log.i("GAME","ONPause");
        backgroundMusic.pause();    }


    //Listen to the accelerometer when user is back on the app
    @Override
    protected void onResume() {

        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),SensorManager.SENSOR_DELAY_FASTEST);

        if(isGamePaused == true) {
            showPauseMenu();
        }
        else {
            Log.i("GAME","ONRESUME1");
            backgroundMusic.start();
        }

//        super.onPause();
//        mSensorManager.registerListener(this, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);
    }

    private void showPauseMenu() {

        view.pause();

        AlertDialog dial = new AlertDialog.Builder(GameActivity.this)
                .setTitle("Paused")
                .setCancelable(false)

                .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isGamePaused =false;
                        view.unPause();
                        Log.i("GAME","ONRESUME2");
                        backgroundMusic.start();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    @Override
    protected void onStop()
    {

        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
        isGamePaused = true;
        Log.i("GAME","ONSTOP");
        backgroundMusic.pause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("GAME","ONDestroy");
        backgroundMusic.stop();
        backgroundMusic.release();
    }


    private final Handler m_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_ALERT:
                    Log.i("ICI","GameActivity");
                    view.pause();
                    AlertDialog dial = new AlertDialog.Builder(GameActivity.this)
                            .setTitle(msg.getData().getString(Constants.ALERT_TITLE))
                            .setCancelable(false)
                            .setMessage("The game is over ! Want to play again?")
                            .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recreate();
                                }
                            })
                            .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
            }

        }
    };
/*
    public class NetworkStateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d("app","Network connectivity change");
            if(intent.getExtras()!=null) {
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
                    Log.i("APP","Network "+ni.getTypeName()+" connected");
                    Toast.makeText(context, "Lost internet connection", Toast.LENGTH_SHORT);
                } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
                    Log.d("app","There's no network connectivity");
                }
            }
        }

    }
*/
}
