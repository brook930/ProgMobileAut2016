package com.nonamestudio.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import Game.GameManager;

class Drawable {

    public Bitmap image;
    public int x;
    public int y;

    public int zOrder;

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

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inScaled = false;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        imagesTable.put("boxer", BitmapFactory.decodeResource(getResources(), R.drawable.boxer, options));
        imagesTable.put("boxer", Bitmap.createScaledBitmap(imagesTable.get("boxer"),
                (int)(imagesTable.get("boxer").getWidth() * ((float)width / imagesTable.get("boxer").getWidth()) * 2.3),
                (int)(imagesTable.get("boxer").getHeight() * ((float)width / imagesTable.get("boxer").getWidth()) * 2.3), true));

        imagesTable.put("background", BitmapFactory.decodeResource(getResources(), R.drawable.background, options));
        imagesTable.put("background", Bitmap.createScaledBitmap(imagesTable.get("background"),
                (int)(imagesTable.get("background").getWidth() * ((float)width / imagesTable.get("background").getWidth())),
                (int)(imagesTable.get("background").getHeight() * ((float)width / imagesTable.get("background").getWidth())), true));

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

    public static void addElementToDraw(Bitmap element, float x, float y, String pos, int zOrder)
    {

        if(pos.equals("center"))
            addElementToDraw(element, x, y, 0.5f, 0.5f, zOrder);
        else if(pos.equals("top"))
            addElementToDraw(element, x, y, 0.5f, 0.0f, zOrder);
        else if(pos.equals("topLeft"))
            addElementToDraw(element, x, y, 0.0f, 0.0f, zOrder);
        else if(pos.equals("topRight"))
            addElementToDraw(element, x, y, 1.0f, 0.0f, zOrder);
        else if(pos.equals("bottom"))
            addElementToDraw(element, x, y, 0.5f, 1.0f, zOrder);
        else if(pos.equals("bottomLeft"))
            addElementToDraw(element, x, y, 0.0f, 1.0f, zOrder);
        else if(pos.equals("bottomRight"))
            addElementToDraw(element, x, y, 1.0f, 1.0f, zOrder);
        else if(pos.equals("left"))
            addElementToDraw(element, x, y, 0.0f, 0.5f, zOrder);
        else if(pos.equals("right"))
            addElementToDraw(element, x, y, 1.0f, 0.5f, zOrder);

    }

    public static void addElementToDraw(Bitmap element, float x, float y)
    {

        addElementToDraw(element, x, y, 0.0f, 0.0f, 0);

    }

    public static void addElementToDraw(Bitmap element, float x, float y, float offsetX, float offsetY, int zOrder)
    {

        if((x < 0.0f || x > 1.0f) || (y < 0.0f || y > 1.0f))
            return;

        addElementToDraw(element,
                (int)(x * MainThread.canvas.getWidth() - (offsetX * element.getWidth())),
                (int)(y * MainThread.canvas.getHeight() - (offsetY * element.getHeight())), zOrder);

    }

    public static void addElementToDraw(Bitmap element, int x, int y, int zOrder)
    {

        Drawable drawable = new Drawable();
        drawable.image = element;
        drawable.x = x;
        drawable.y = y;
        drawable.zOrder = zOrder;

        elementsToDraw.add(drawable);

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        drawImage(canvas, imagesTable.get("background"), 0, 0);



        Collections.sort(elementsToDraw, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable d1, Drawable d2) {
                return d1.zOrder - d2.zOrder;
            }
        });

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

    public void update()
    {

        gameManager.update();

    }

}