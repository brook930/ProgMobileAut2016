package Game;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.nonamestudio.mobileproject.GameActivity;
import com.nonamestudio.mobileproject.MainActivity;
import com.nonamestudio.mobileproject.ViewInGame;

import java.io.FileDescriptor;
import java.io.PrintWriter;
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
    private long startDodgeIA = 0;
    private long cooldownIA = 3000;
    private long cooldownDodgeIA = 5;

    private Hashtable<ViewInGame.Input, InputInteraction> inputInteractions;

    boolean vsIA;
    SoundManager m_soundManager;

    private Context m_context;

    private Handler m_handler;

    public GameManager(boolean vsIA, SoundManager soundManager, Context context, Handler handler)
    {

        m_currentPlayer = new Character(15, true, this);

        m_enemyPlayer = new Character(15, false, this);

        this.vsIA = vsIA;

        m_soundManager = soundManager;

        inputInteractions = new Hashtable<>();

        inputInteractions.put(ViewInGame.Input.LEFTYROT, new InputInteraction(Interaction.PUNCH, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTYROT, new InputInteraction(Interaction.PUNCH, Direction.RIGHT));
        inputInteractions.put(ViewInGame.Input.LEFTZROT, new InputInteraction(Interaction.DODGE, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTZROT, new InputInteraction(Interaction.DODGE, Direction.RIGHT));
        inputInteractions.put(ViewInGame.Input.LEFTTOUCH, new InputInteraction(Interaction.FAKE, Direction.LEFT));
        inputInteractions.put(ViewInGame.Input.RIGHTTOUCH, new InputInteraction(Interaction.FAKE, Direction.RIGHT));

        m_context = context;
        m_handler = handler;
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
        int dodgeChance = 10;

        int punchChance = 40;
        int fakeChance = 65;

        int punchWhileDodgeChance = 20;


        // Player Inputs
        Action playerActions = m_currentPlayer.getActions();
        Action enemyActions = m_enemyPlayer.getActions();

        if(input != ViewInGame.Input.NONE)
        {
            playerActions.setInteractionState(inputInteractions.get(input).m_interaction, true);
            playerActions.setInteractionDirection(inputInteractions.get(input).m_direction);
        }

        // Enemy inputs
        long deltaTime = System.currentTimeMillis() - startCooldownIA;
        long deltaTimeDodge = System.currentTimeMillis() - startDodgeIA;

        if( deltaTime > cooldownDodgeIA / 10 &&  (m_currentPlayer.state.equals(CharState.PREPARINGPUNCH) || m_currentPlayer.state.equals(CharState.FAKING)))
        {
            int randValue = new Random().nextInt(100);
            if( randValue < dodgeChance)
            {
                enemyActions.setInteractionState( Interaction.DODGE, true);

                enemyActions.setInteractionDirection(  m_currentPlayer.getActions().direction.equals(Direction.LEFT) ? Direction.RIGHT : Direction.LEFT);
            }

            startDodgeIA = System.currentTimeMillis();
        }

        if( deltaTime > cooldownIA) // Les actions aggressives de l'IA
        {
            int randValue = new Random().nextInt(100);

            if( m_currentPlayer.state.equals(CharState.DODGING))
            {
                if( randValue < punchWhileDodgeChance) // Chance de frapper la ou le joueur est en train d'esquiver
                {
                    enemyActions.setInteractionState(Interaction.PUNCH, true);
                    enemyActions.setInteractionDirection( m_currentPlayer.getActions().direction.equals(Direction.LEFT) ? Direction.LEFT : Direction.RIGHT);
                    startCooldownIA = System.currentTimeMillis();
                }
            }
            else
            {
                if( randValue < punchChance)
                {
                    enemyActions.setInteractionState(Interaction.PUNCH, true);

                    enemyActions.setInteractionDirection( new Random().nextInt(100) < 50 ? Direction.RIGHT : Direction.LEFT);

                    startCooldownIA = System.currentTimeMillis();
                }
                else if( randValue >= punchChance && randValue < fakeChance)
                {
                    enemyActions.setInteractionState(Interaction.FAKE, true);

                    enemyActions.setInteractionDirection( new Random().nextInt(100) < 50 ? Direction.RIGHT : Direction.LEFT);

                    startCooldownIA = System.currentTimeMillis();
                }
                else
                {
                    startCooldownIA = System.currentTimeMillis();
                }

            }

        }




    }

    public void gameOver(boolean playerWins)
    {
        if( playerWins)
            showAlert("YOU WIN");
        //showAlert("YOU");
        else
            showAlert("THE AI WINS");

    }

    public void hit(boolean isPlayerSource)
    {
        if( isPlayerSource)
        {
            if(m_enemyPlayer.state != CharState.DODGING)
            {
                m_soundManager.hitSound.start();
                m_enemyPlayer.changeState(CharState.DAMAGED);
                m_enemyPlayer.receiveDamage(1);
                m_enemyPlayer.m_anim.playAnim("hit");
            }
        }
        else
        {
            if(m_currentPlayer.state != CharState.DODGING)
            {
                m_soundManager.hitSound.start();
                m_currentPlayer.changeState(CharState.DAMAGED);
                m_currentPlayer.receiveDamage(1);
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

    private void showAlert(String victoriousP)
    {

        Log.i("ICI","ICI");
        Message msg = m_handler.obtainMessage(Constants.MESSAGE_ALERT);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ALERT_TITLE, victoriousP);
        msg.setData(bundle);
        m_handler.sendMessage(msg);
    }


}
