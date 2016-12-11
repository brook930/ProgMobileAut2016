package com.nonamestudio.mobileproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sManager;
    private long timeSinceLastDodge;

    private ViewInGame view;

    private boolean isGamePaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        view = new ViewInGame(this);
        setContentView(view);

        timeSinceLastDodge = SystemClock.uptimeMillis();

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
    }


    //Listen to the accelerometer when user is back on the app
    @Override
    protected void onResume() {

        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),SensorManager.SENSOR_DELAY_FASTEST);

//        super.onPause();
//        mSensorManager.registerListener(this, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop()
    {

        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();

    }
}
