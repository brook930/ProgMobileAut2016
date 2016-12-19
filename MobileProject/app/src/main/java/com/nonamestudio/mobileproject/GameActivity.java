package com.nonamestudio.mobileproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import Game.Constants;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sManager;
    private long timeSinceLastDodge;

    private ViewInGame view;

    private boolean isGamePaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        // turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        view = new ViewInGame(this, m_handler);
        setContentView(view);

        timeSinceLastDodge = SystemClock.uptimeMillis();

        TextView playerName = new TextView(this);
        playerName.setText("q");

        /*
         GraphRequest request = GraphRequest.newMeRequest(
         loginResult.getAccessToken(),
         new GraphRequest.GraphJSONObjectCallback() {

        @Override
        public void onCompleted(
        JSONObject object,
        GraphResponse response) {

        JSONObject json = response.getJSONObject();
        try {
        if(json != null){
        String userName = json.getString("first_name") + " " + json.getString("last_name");
        Toast.makeText(MainActivity.this, "Welcome " + userName, Toast.LENGTH_LONG).show();
        }

        } catch (JSONException e) {
        e.printStackTrace();
        }
        }

        });*/
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

}
