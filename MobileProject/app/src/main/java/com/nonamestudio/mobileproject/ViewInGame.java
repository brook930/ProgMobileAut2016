package com.nonamestudio.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import Game.GameManager;
import Game.SoundManager;

class Drawable {

    public Bitmap image;
    public int x;
    public int y;

    public int pX;
    public int pY;

    public float scaleX;
    public float scaleY;

    public int zOrder;

}

public class ViewInGame extends SurfaceView implements SurfaceHolder.Callback {

    public MainThread thread;

    private Paint mPaint;
    private float mFontSize;

    public enum Input{

        LEFTYROT,
        RIGHTYROT,
        LEFTZROT,
        RIGHTZROT,
        LEFTTOUCH,
        RIGHTTOUCH,
        NONE

    }

    public Input input = Input.NONE;

    static public Hashtable<String, Bitmap> imagesTable = new Hashtable<String, Bitmap>();

    static private ArrayList<Drawable> elementsToDraw = new ArrayList<Drawable>();
    Comparator<Drawable> comparatorDrawable;

    private SoundManager soundManager;
    private GameManager gameManager;

    private float backgroundScale;
    private Bitmap background;

    private Handler m_handler;

    public ViewInGame(Context context, Handler handler) {
        super(context);

        mFontSize = 17 * getResources().getDisplayMetrics().density;

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mFontSize);

        // textures loading
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        imagesTable.put("boxer", BitmapFactory.decodeResource(getResources(), R.drawable.boxer, options));
        background =  BitmapFactory.decodeResource(getResources(), R.drawable.background, options);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        backgroundScale = (float)width / background.getWidth();

        soundManager = new SoundManager(context);
        m_handler = handler;
        gameManager = new GameManager(true, soundManager, getContext(), m_handler);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // Make ViewInGame focusable so it can handle events
        setFocusable(true);

        comparatorDrawable = new Comparator<Drawable>() {
            @Override
            public int compare(Drawable d1, Drawable d2) {
                return d1.zOrder - d2.zOrder;
            }};

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

    public static void addElementToDraw(Bitmap element, float x, float y, String pos, int zOrder, float scaleX, float scaleY)
    {

        if(pos.equals("center"))
            addElementToDraw(element, x, y, 0.5f, 0.5f, zOrder, scaleX, scaleY);
        else if(pos.equals("top"))
            addElementToDraw(element, x, y, 0.5f, 0.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("topLeft"))
            addElementToDraw(element, x, y, 0.0f, 0.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("topRight"))
            addElementToDraw(element, x, y, 1.0f, 0.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("bottom"))
            addElementToDraw(element, x, y, 0.5f, 1.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("bottomLeft"))
            addElementToDraw(element, x, y, 0.0f, 1.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("bottomRight"))
            addElementToDraw(element, x, y, 1.0f, 1.0f, zOrder, scaleX, scaleY);
        else if(pos.equals("left"))
            addElementToDraw(element, x, y, 0.0f, 0.5f, zOrder, scaleX, scaleY);
        else if(pos.equals("right"))
            addElementToDraw(element, x, y, 1.0f, 0.5f, zOrder, scaleX, scaleY);

    }

    public static void addElementToDraw(Bitmap element, float x, float y)
    {

        addElementToDraw(element, x, y, 0.0f, 0.0f, 0, 1.0f, 1.0f);

    }

    public static void addElementToDraw(Bitmap element, float x, float y, float offsetX, float offsetY, int zOrder, float scaleX, float scaleY)
    {

        if((x < 0.0f || x > 1.0f) || (y < 0.0f || y > 1.0f))
            return;

        addElementToDraw(element,
                (int)(x * MainThread.canvas.getWidth() - (offsetX * element.getWidth())),
                (int)(y * MainThread.canvas.getHeight() - (offsetY * element.getHeight())),
                zOrder, scaleX, scaleY,
                (int)(offsetX * element.getWidth()),
                (int)(offsetY * element.getHeight()));

    }

    public static void addElementToDraw(Bitmap element, int x, int y, int zOrder, float scaleX, float scaleY, int px, int py)
    {

        Drawable drawable = new Drawable();
        drawable.image = element;
        drawable.x = x;
        drawable.y = y;
        drawable.pX = px;
        drawable.pY = py;
        drawable.zOrder = zOrder;
        drawable.scaleX = scaleX;
        drawable.scaleY = scaleY;

        elementsToDraw.add(drawable);

    }

    @Override
    public void draw(Canvas canvas) {

        //super.draw(canvas);

        drawImage(canvas, background, 0, 0, 0, 0, backgroundScale, backgroundScale);

        Collections.sort(elementsToDraw, comparatorDrawable);

        for(int i = 0; i < elementsToDraw.size(); i++)
        {

            drawImage(canvas, elementsToDraw.get(i).image,
                    elementsToDraw.get(i).x,
                    elementsToDraw.get(i).y,
                    elementsToDraw.get(i).pX,
                    elementsToDraw.get(i).pY,
                    elementsToDraw.get(i).scaleX,
                    elementsToDraw.get(i).scaleY);

        }

        elementsToDraw.clear();

    }



    private void drawImage(Canvas canvas, Bitmap image, int x, int y, int px, int py, float scaleX, float scaleY)
    {

        Matrix matrix = new Matrix();

        matrix.setScale(scaleX, scaleY, px, py);
        matrix.postTranslate(x, y);

        canvas.drawBitmap(image, matrix, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int centerX = displaymetrics.widthPixels / 2;

        if(event.getAxisValue(0) <= centerX) {

            Log.i("pouet", "touch left");
            input = Input.LEFTTOUCH;

        }
        else if (event.getAxisValue(0) > centerX) {

            Log.i("pouet", "touch right");
            input = Input.RIGHTTOUCH;

        }

        return true;
    }

    public void update()
    {

        gameManager.updateInputs(input);

        gameManager.update();

        input = Input.NONE;

    }


}