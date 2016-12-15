package Game;

import com.nonamestudio.mobileproject.ViewInGame;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

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

    private long startCooldownIA = 0;
    private long cooldownIA = 5000;

    private Hashtable<ViewInGame.Input, InputInteraction> inputInteractions;

    boolean vsIA;
    SoundManager m_soundManager;

    public GameManager(boolean vsIA, SoundManager soundManager)
    {

        m_currentPlayer = new Character(3, true, this);

        m_enemyPlayer = new Character(3, false, this);

        this.vsIA = vsIA;

        m_soundManager = soundManager;

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
        int dodgePunchChance = 35;
        int dodgeFakeChance = 20;

        int punchChance = 40;
        int fakeChance = 65;

        int punchWhileDodgeChance = 20;



        Action playerActions = m_currentPlayer.getActions();
        Action enemyActions = m_enemyPlayer.getActions();

        if(input != ViewInGame.Input.NONE)
        {
            playerActions.setInteractionState(inputInteractions.get(input).m_interaction, true);
            playerActions.setInteractionDirection(inputInteractions.get(input).m_direction);
        }


        if( System.currentTimeMillis() - startCooldownIA > cooldownIA);
        {
            int randValue = new Random().nextInt(100);

            if( playerActions.getInteractionState(Interaction.PUNCH)) // Si le joueur ne tape pas
            {
                if( playerActions.getDirection() == Direction.LEFT && randValue < dodgePunchChance) // 25% de chance d'éviter si attaque gauche
                {
                    enemyActions.setInteractionState( Interaction.DODGE, true);
                    enemyActions.setInteractionDirection(Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
                else if( playerActions.getDirection() == Direction.RIGHT && randValue < dodgePunchChance) // 25% de chance d'éviter si attaque droite
                {
                    enemyActions.setInteractionState( Interaction.DODGE, true);
                    enemyActions.setInteractionDirection(Direction.LEFT);
                    startCooldownIA = System.currentTimeMillis();
                }
            }
            else if( playerActions.getInteractionState(Interaction.FAKE))
            {
                if( playerActions.getDirection() == Direction.LEFT && randValue < dodgeFakeChance) // 25% de chance d'éviter si attaque gauche
                {
                    enemyActions.setInteractionState( Interaction.DODGE, true);
                    enemyActions.setInteractionDirection(Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
                else if( playerActions.getDirection() == Direction.RIGHT && randValue < dodgeFakeChance) // 25% de chance d'éviter si attaque droite
                {
                    enemyActions.setInteractionState( Interaction.DODGE, true);
                    enemyActions.setInteractionDirection(Direction.LEFT);
                    startCooldownIA = System.currentTimeMillis();
                }
            }
            else if( playerActions.getInteractionState(Interaction.DODGE))
            {
                if( playerActions.getDirection() == Direction.LEFT && randValue < punchWhileDodgeChance) // Chance de frapper la ou le jouur est en train d'esquiver
                {
                    enemyActions.setInteractionState(Interaction.PUNCH, true);
                    enemyActions.setInteractionDirection(Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
                if( playerActions.getDirection() == Direction.RIGHT && randValue < punchWhileDodgeChance) // Chance de frapper la ou le jouur est en train d'esquiver
                {
                    enemyActions.setInteractionState(Interaction.PUNCH, true);
                    enemyActions.setInteractionDirection(Direction.LEFT);
                    startCooldownIA = System.currentTimeMillis();
                }
            }
            else
            {
                if( randValue < punchChance)
                {
                    //TODO : Remettre ton putain de single line statement
                    enemyActions.setInteractionState(Interaction.PUNCH, true);
                    enemyActions.setInteractionDirection(Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
                else if( randValue >= punchChance && randValue < fakeChance)
                {
                    //TODO : Remettre ton putain de single line statement
                    enemyActions.setInteractionState(Interaction.FAKE, true);
                    enemyActions.setInteractionDirection(Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
            }
        }




    }

    public int gameOver()
    {

        return 0;

    }

    public void hit(boolean isPlayerSource)
    {
        if( isPlayerSource)
        {
            if(m_enemyPlayer.state != CharState.DODGING)
            {
                m_soundManager.hitSound.start();
                m_enemyPlayer.state = CharState.DAMAGED;
                m_enemyPlayer.m_anim.playAnim("hit");
            }
        }
        else
        {
            if(m_currentPlayer.state != CharState.DODGING)
            {
                m_soundManager.hitSound.start();
                m_currentPlayer.state = CharState.DAMAGED;
                m_currentPlayer.m_anim.playAnim("hit");
            }
        }
    }

    public void playSound(String soundToPlay)
    {
        soundToPlay.toLowerCase();

        switch(soundToPlay)
        {
            case "prepPunch":
                m_soundManager.preparePunchSound.start();
                break;

            case "punch":
                m_soundManager.punchSound.start();
                break;

            case "dodge":
                m_soundManager.dodgeSound.start();
                break;

            case "fake":
                break;

            case "hit":
                m_soundManager.hitSound.start();
                break;
        }
    }
}
