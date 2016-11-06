package Game;

import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.nonamestudio.mobileproject.ViewInGame;

/**
 * Created by Maxime on 2016-11-05.
 */

public class GameManager {

    Character m_player1;
    Character m_player2;

    ViewInGame m_context;

    long startTime;

    public GameManager(ViewInGame context)
    {

        m_player1 = new Character(3, true);
        m_player2 = new Character(3, false);

        m_context = context;

    }

    public void update()
    {

        m_player1.update();
        m_player2.update();

    }

    public int gameOver()
    {

        return 0;

    }

}
