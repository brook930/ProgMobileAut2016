package Game;

import com.nonamestudio.mobileproject.ViewInGame;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maxime on 2016-11-05.
 */

class InputInteraction
{

    public Interaction         m_interaction;
    public Direction           m_direction;

    public InputInteraction(Interaction interaction, Direction direction)
    {

        m_interaction = interaction; m_direction = direction;

    }

}

public class GameManager {

    Character m_currentPlayer;
    Character m_enemyPlayer;

    private Hashtable<ViewInGame.Input, InputInteraction> inputInteractions;

    boolean vsIA;

    public GameManager(boolean vsIA)
    {

        m_currentPlayer = new Character(3, true, this);

        m_enemyPlayer = new Character(3, false, this);

        this.vsIA = vsIA;

        inputInteractions = new Hashtable<>();

        inputInteractions.put(ViewInGame.Input.LEFTYROT, new InputInteraction(Interaction.PUNCH, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTYROT, new InputInteraction(Interaction.PUNCH, Direction.RIGHT));
        inputInteractions.put(ViewInGame.Input.LEFTZROT, new InputInteraction(Interaction.DODGE, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTZROT, new InputInteraction(Interaction.DODGE, Direction.RIGHT));
        inputInteractions.put(ViewInGame.Input.LEFTTOUCH, new InputInteraction(Interaction.FAKE, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTTOUCH, new InputInteraction(Interaction.FAKE, Direction.RIGHT));


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
        Action enemyActions = m_enemyPlayer.getActions();

        playerActions.resetInteractionStates();

        if(input != ViewInGame.Input.NONE) {

            playerActions.resetInteractionDirection();

            playerActions.setInteractionState(inputInteractions.get(input).m_interaction, true);
            playerActions.setInteractionDirection(inputInteractions.get(input).m_direction);

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
