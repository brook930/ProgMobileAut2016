package Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nonamestudio.mobileproject.R;
import com.nonamestudio.mobileproject.ViewInGame;

import java.util.Hashtable;

/**
 * Created by Maxime on 2016-11-05.
 */

class Frame
{

    public Frame(int x, int y, int w, int h) { this.x = x; this.y = y; this.w = w; this.h = h; }

    int x;
    int y;

    int w;
    int h;

}

public class Anim {

    boolean m_isForeground;

    Bitmap spriteSheet;
    Hashtable<String, Frame> frameTable = new Hashtable<String, Frame>();

    public Anim(boolean isForeground)
    {

        spriteSheet = ViewInGame.imagesTable.get("boxer");

        frameTable.put("idle1", new Frame(0,0, 20, 20));



    }

    public void update()
    {

        Bitmap bPrime = Bitmap.createBitmap(spriteSheet, 600, 0, 230, 550);

        ViewInGame.addElementToDraw(bPrime, 0, 0);

    }

    public boolean isForeground() {
        return m_isForeground;
    }

    public void setIsForeground(boolean isForeground) {
        this.m_isForeground = isForeground;
    }

}
