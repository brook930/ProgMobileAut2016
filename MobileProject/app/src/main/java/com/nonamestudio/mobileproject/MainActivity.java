package com.nonamestudio.mobileproject;

/**
 * Created by melvi_000 on 09/12/2016.
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private ImageButton buttonPlay;

    private ImageButton buttonQuit;

    private MediaPlayer backgroundMusic;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        backgroundMusic = MediaPlayer.create(this, R.raw.main_menu_theme);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonQuit = (ImageButton) findViewById(R.id.buttonQuit);

        //adding a click listener
        buttonPlay.setOnClickListener(this);
        buttonQuit.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
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
                                        Toast.makeText(MainActivity.this, "Welcome " + userName, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                //Mandatory
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "You canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        //To obtain the key sent to facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nonamestudio.mobileproject", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.buttonPlay :
                AccessToken tokenIsLoggedIn = AccessToken.getCurrentAccessToken();

                //User is connected
                if(tokenIsLoggedIn != null) {
                    //starting game activity
                    startActivity(new Intent(this, GameActivity.class));
                }
                else {
                    Toast.makeText(this, "You must be logged in Facebook", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonQuit :
                finish();
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.i("MUSIC","ONPause");
        backgroundMusic.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (backgroundMusic == null) {
            backgroundMusic = MediaPlayer.create(this, R.raw.main_menu_theme);
            backgroundMusic.setLooping(true);

            backgroundMusic.start();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("MUSIC","ONSTOP");
        backgroundMusic.stop();
        backgroundMusic.release();
        backgroundMusic = null;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("MUSIC","ONDestroy");
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
        }
    }
}
