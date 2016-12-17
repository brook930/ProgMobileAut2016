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

    public Frame(int x, int y, int w, int h, Bitmap spriteSheet)
    {

        this.x = x; this.y = y; this.w = w; this.h = h;

        bitmap = Bitmap.createBitmap(spriteSheet, x, y, w, h);

    }

    Bitmap bitmap;

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

        if(frames.size() > 1) {

            int timeFromStart = (int) ((System.nanoTime() / 1000000) - startTime);

            if (timeFromStart > timeSteps.get(currentStep + 1)) {

                currentStep++;
                currentFrame = frames.get(currentStep);

            }

            if (currentStep == frames.size() - 1)
                start();

        }

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

    private float scale = 1.0f;

    public Anim(boolean isForeground)
    {

        spriteSheet = ViewInGame.imagesTable.get("boxer");

        m_isForeground = isForeground;
        if(isForeground)
            m_zOrder = 10;
        else
            m_zOrder = 0;

        timelines.put("idle", new Timeline());
        timelines.put("prepPunch", new Timeline());
        timelines.put("punch", new Timeline());
        timelines.put("hit", new Timeline());
        timelines.put("dodge", new Timeline());
        timelines.put("fake", new Timeline());

        if(!isForeground) {

            timelines.get("idle").addStep(new Frame(123, 5, 44, 83, spriteSheet), 0);

            timelines.get("idle").addStep(new Frame(123, 2, 47, 83, spriteSheet), 220);

            timelines.get("idle").addStep(new Frame(48, 5, 44, 83, spriteSheet), 440);

            timelines.get("idle").addStep(new Frame(45, 2, 47, 83, spriteSheet), 660);

            timelines.get("idle").addStep(new Frame(123, 5, 44, 83, spriteSheet), 880);





            timelines.get("prepPunch").addStep(new Frame(87, 127, 48, 83, spriteSheet), 0);




            timelines.get("punch").addStep(new Frame(197, 5, 44, 83, spriteSheet), 0);



            
            timelines.get("hit").addStep(new Frame(273, 3, 48, 85, spriteSheet), 0);




            timelines.get("dodge").addStep(new Frame(206, 121, 41, 84, spriteSheet), 0);

        }
        else
        {

            timelines.get("idle").addStep(new Frame(3, 285, 43, 49, spriteSheet), 0);

            timelines.get("idle").addStep(new Frame(3, 285, 46, 46, spriteSheet), 200);

            timelines.get("idle").addStep(new Frame(3, 285, 43, 49, spriteSheet), 400);

            timelines.get("idle").addStep(new Frame(0, 285, 46, 46, spriteSheet), 600);

            timelines.get("idle").addStep(new Frame(0, 285, 46, 46, spriteSheet), 800);



            timelines.get("prepPunch").addStep(new Frame(287, 286, 47, 49, spriteSheet), 0);



            timelines.get("punch").addStep(new Frame(49, 270, 34, 64, spriteSheet), 0);



            timelines.get("hit").addStep(new Frame(97, 277, 41, 57, spriteSheet), 0);



            timelines.get("dodge").addStep(new Frame(203, 286, 65, 49, spriteSheet), 0);
            //timelines.get("dodge").addStep(new Frame(317, 288, 73, 48, spriteSheet), 0);

        }

        currentTimeline = timelines.get("idle");

        currentTimeline.start();

    }

    public void playAnim(String animName)
    {

        currentTimeline = timelines.get(animName);

        currentTimeline.start();

    }

    public void update(Direction direction)
    {

        currentTimeline.update();

        float scaleX = 12.0f;
        float scaleY = 12.0f;

        if(direction == Direction.LEFT)
            scaleX = -scaleX;

        if(m_isForeground)
            ViewInGame.addElementToDraw(currentTimeline.currentFrame.bitmap, 0.5f, 1.0f, "bottom", m_zOrder, scaleX, scaleY);
        else
            ViewInGame.addElementToDraw(currentTimeline.currentFrame.bitmap, 0.5f, 0.2f, "top", m_zOrder, scaleX, scaleY);

    }

}
