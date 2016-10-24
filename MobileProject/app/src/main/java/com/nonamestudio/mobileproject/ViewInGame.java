package com.nonamestudio.mobileproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViewInGame extends View {

    private Paint mPaint;
    private float mFontSize;

    int px = 400;
    int py = 800;

    int dx = -10;


    public ViewInGame(Context context) {
        super(context);

        mFontSize = 17 * getResources().getDisplayMetrics().density;

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mFontSize);


        Log.i("info", " color " + mPaint.getColor());

        //mPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);
        RenderShip(canvas);

        canvas.drawText("The score goes here : " , 20, 60, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        invalidate();
        return true;
    }


    void drawShip(Canvas can, Bitmap b, int x, int y)
    {
        int w = 50;
        int h= 100;
       /* Drawable d = getContext().getResources().getDrawable(R.drawable.ship);
        d.setBounds(x-w,y+h,x+w,y-h);;
        d.draw(can);*/


        mPaint.setColor(Color.RED);
        can.drawBitmap(b, x, y, mPaint);


        Log.i("info", " in draw");
        Log.i("info", " color " + mPaint.getColor());
    }

    void RenderShip(Canvas can)
    {

        Bitmap b= BitmapFactory.decodeResource(getResources(), R.mipmap.ship);

        drawShip(can, b, px, py);

        if (px < 0) dx=10;
        if (px > can.getWidth() - b.getWidth()) dx=-10;


        px+=dx;

        invalidate();
        Log.i("info", " px : " + px);

    }

}