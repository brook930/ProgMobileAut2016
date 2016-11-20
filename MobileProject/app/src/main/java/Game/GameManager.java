package Game;

import com.nonamestudio.mobileproject.ViewInGame;

/**
 * Created by Maxime on 2016-11-05.
 */

public class GameManager {

    Character m_currentPlayer;
    Character m_enemyPlayer;

    boolean vsIA;

    public GameManager(boolean vsIA)
    {

        m_currentPlayer = new Character(3, true, this);

        m_enemyPlayer = new Character(3, false, this);

        this.vsIA = vsIA;

    }

    public void update()
    {

        if(m_currentPlayer != null)
            m_currentPlayer.update();
        if(m_enemyPlayer != null)
            m_enemyPlayer.update();

    }

    public void updateInputs(ViewInGame.Input input)
    {

        Action playerActions = m_currentPlayer.getActions();
        //Action enemyActions = m_enemyPlayer.getActions();

        playerActions.resetInteractionStates();

        if(input == ViewInGame.Input.LEFTYROT)
        {

            playerActions.setInteractionState(Interaction.PUNCH, true);

        }

    }

    public int gameOver()
    {

        return 0;

    }

    public void hit()
    {

        if(m_enemyPlayer.state != CharState.DODGING)
        {

            m_enemyPlayer.state = CharState.DAMAGED;
            m_enemyPlayer.m_anim.playAnim("hit");

        }

    }

}
