package Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.nonamestudio.mobileproject.ViewInGame;

import java.util.ArrayList;
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

class Timeline
{

    ArrayList<Frame> frames = new ArrayList<Frame>();
    ArrayList<Integer> timeSteps = new ArrayList<Integer>();

    public Frame currentFrame;
    public Integer currentStep;

    int startTime;

    boolean isPlaying;

    public void start()
    {

        currentStep = 0;
        currentFrame = frames.get(currentStep);

        startTime = (int)(System.nanoTime() / 1000000);

        isPlaying = true;

    }

    public void stop()
    {

        startTime = -1;
        isPlaying = false;

    }

    public void update()
    {

        int timeFromStart = (int)((System.nanoTime() / 1000000) - startTime);

        if(timeFromStart > timeSteps.get(currentStep + 1))
        {

            currentStep++;
            currentFrame = frames.get(currentStep);

        }

        if(currentStep == frames.size() - 1)
            start();

    }

    void addStep(Frame frame, int timeStep)
    {

        frames.add(frame);
        timeSteps.add(timeStep);

    }

}

public class Anim {

    boolean m_isForeground;

    Bitmap spriteSheet;
    private Hashtable<String, Timeline> timelines = new Hashtable<String, Timeline>();

    private Timeline currentTimeline;

    private int m_zOrder = 0;

    public Anim(boolean isForeground, int zOrder)
    {

        spriteSheet = ViewInGame.imagesTable.get("boxer");

        m_isForeground = isForeground;
        if(isForeground)
            m_zOrder = 10;
        else
            m_zOrder = 0;

        timelines.put("idle", new Timeline());

        if(!isForeground) {
            timelines.get("idle").addStep(new Frame(
                    (int) (0.011601 * spriteSheet.getWidth()),
                    (int) (0.003738 * spriteSheet.getHeight()),
                    (int) (0.088167 * spriteSheet.getWidth()),
                    (int) (0.155140 * spriteSheet.getHeight())), 0);

            timelines.get("idle").addStep(new Frame(
                    (int) (0.122970 * spriteSheet.getWidth()),
                    (int) (0.003738 * spriteSheet.getHeight()),
                    (int) (0.088167 * spriteSheet.getWidth()),
                    (int) (0.155140 * spriteSheet.getHeight())), 200);

            timelines.get("idle").addStep(new Frame(
                    (int) (0.011601 * spriteSheet.getWidth()),
                    (int) (0.003738 * spriteSheet.getHeight()),
                    (int) (0.088167 * spriteSheet.getWidth()),
                    (int) (0.155140 * spriteSheet.getHeight())), 400);
        }
        else
        {

            timelines.get("idle").addStep(new Frame(
                    (int) (0.006961 * spriteSheet.getWidth()),
                    (int) (0.532710 * spriteSheet.getHeight()),
                    (int) (0.099768 * spriteSheet.getWidth()),
                    (int) (0.091589 * spriteSheet.getHeight())), 0);

            timelines.get("idle").addStep(new Frame(
                    (int) (0.350348 * spriteSheet.getWidth()),
                    (int) (0.532710 * spriteSheet.getHeight()),
                    (int) (0.078886 * spriteSheet.getWidth()),
                    (int) (0.093458 * spriteSheet.getHeight())), 200);

            timelines.get("idle").addStep(new Frame(
                    (int) (0.006961 * spriteSheet.getWidth()),
                    (int) (0.532710 * spriteSheet.getHeight()),
                    (int) (0.099768 * spriteSheet.getWidth()),
                    (int) (0.091589 * spriteSheet.getHeight())), 400);

        }

        currentTimeline = timelines.get("idle");

        currentTimeline.start();

    }

    public void update()
    {

        currentTimeline.update();

        Bitmap bPrime = Bitmap.createBitmap(spriteSheet,
                currentTimeline.currentFrame.x,
                currentTimeline.currentFrame.y,
                currentTimeline.currentFrame.w,
                currentTimeline.currentFrame.h);

        if(m_isForeground)
            ViewInGame.addElementToDraw(bPrime, 0.5f, 1.0f, "bottom", m_zOrder);
        else
            ViewInGame.addElementToDraw(bPrime, 0.5f, 0.1f, "top", m_zOrder);


    }

}
