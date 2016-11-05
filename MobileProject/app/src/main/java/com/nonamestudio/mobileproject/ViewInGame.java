package com.nonamestudio.mobileproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

class Drawable {

    public Bitmap image;
    public int x;
    public int y;

}

public class ViewInGame extends View {

    private Paint mPaint;
    private float mFontSize;

    private ArrayList<Drawable> elementsToDraw = new ArrayList<Drawable>();

    int px = 200;
    int py = 100;

    int dx = -10;


    public ViewInGame(Context context) {
        super(context);

        mFontSize = 17 * getResources().getDisplayMetrics().density;

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mFontSize);


        //Log.i("info", " color " + mPaint.getColor());

    }

    public void addElementToDraw(Bitmap element, int x, int y)
    {

        Drawable drawable = new Drawable();
        drawable.image = element;
        drawable.x = x;
        drawable.y = y;

        elementsToDraw.add(drawable);

    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);

        for(int i = 0; i < elementsToDraw.size(); i++)
        {

            draw(canvas, elementsToDraw.get(i).image,
                    elementsToDraw.get(i).x,
                    elementsToDraw.get(i).y);

        }

        elementsToDraw.clear();

        //canvas.drawText("The score goes here : " , 20, 60, mPaint);

    }

    private void draw(Canvas canvas, Bitmap image, int x, int y)
    {
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(false);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(false);

        canvas.drawBitmap(image, x, y, mPaint);


        Log.i("info", " in draw");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        invalidate();
        return true;
    }

    void RenderShip(Canvas can)
    {

        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.boxer);

        Bitmap bPrime = Bitmap.createBitmap(b, 600, 0, 230, 550);

        Bitmap bPrimePrime = Bitmap.createBitmap(b, 17, 1700, 260, 300);

        if (px < 0) dx=10;
        if (px > can.getWidth() - b.getWidth()) dx=-10;


        px+=dx*0;

        invalidate();
        Log.i("info", " px : " + px);

    }

    Bitmap openImage()
    {

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.boxer);

        return b;

    }

}