package com.nonamestudio.mobileproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import Game.GameManager;

class Drawable {

    public Bitmap image;
    public int x;
    public int y;

}

public class ViewInGame extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private Paint mPaint;
    private float mFontSize;

    static public Hashtable<String, Bitmap> imagesTable = new Hashtable<String, Bitmap>();

    static private ArrayList<Drawable> elementsToDraw = new ArrayList<Drawable>();

    private GameManager gameManager;

    public ViewInGame(Context context) {
        super(context);

        mFontSize = 17 * getResources().getDisplayMetrics().density;

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mFontSize);

        imagesTable.put("boxer", BitmapFactory.decodeResource(getResources(), R.drawable.boxer));

        gameManager = new GameManager(this);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // Make ViewInGame focusable so it can handle events
        setFocusable(true);


        //Log.i("info", " color " + mPaint.getColor());

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            }catch(InterruptedException e) {e.printStackTrace();}
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    public static void addElementToDraw(Bitmap element, int x, int y)
    {

        Drawable drawable = new Drawable();
        drawable.image = element;
        drawable.x = x;
        drawable.y = y;

        elementsToDraw.add(drawable);

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        for(int i = 0; i < elementsToDraw.size(); i++)
        {

            drawImage(canvas, elementsToDraw.get(i).image,
                    elementsToDraw.get(i).x,
                    elementsToDraw.get(i).y);

        }

        elementsToDraw.clear();

    }

    private void drawImage(Canvas canvas, Bitmap image, int x, int y)
    {
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(false);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(false);

        canvas.drawBitmap(image, x, y, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i("pouet", "touch");

        invalidate();
        return true;
    }

    public void display()
    {

        invalidate();

    }

    void RenderShip(Canvas can)
    {

        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.boxer);

        Bitmap bPrime = Bitmap.createBitmap(b, 600, 0, 230, 550);

        Bitmap bPrimePrime = Bitmap.createBitmap(b, 17, 1700, 260, 300);

        invalidate();

    }

    public void update()
    {

        gameManager.update();

    }

}