package Game;

import com.nonamestudio.mobileproject.GameActivity;

/**
 * Created by Maxime on 2016-11-05.
 */

enum CharState
{

    IDLE,
    PREPARINGPUNCH,
    PUNCHING,
    DODGING,
    FAKING,
    DAMAGED

};

public class Character {

    private int m_lifePoints;

    private boolean m_isInvincible;

    public Action actions;
    public Anim m_anim;

    long preparingPunchTime = 200;
    long punchingTime = 300;
    long dodgingTime = 300;
    long fakingTime = 300;
    long damagedTime = 300;

    long punchingCooldown = 0;
    long dodgingCooldown = 0;
    long fakingCooldown = 0;

    long punchingCooldownConstants = 300;
    long dodgingCooldownConstants = 300;
    long fakingCooldownConstants = 300;

    long startTime = 0;
    private boolean isPlayer;

    GameManager gameManager;

    CharState state;

    public Character(int lifePoints, boolean isForeground, GameManager gameManager)
    {

        m_lifePoints = lifePoints;
        m_anim = new Anim(isForeground);
        actions = new Action();

        m_isInvincible = false;
        isPlayer = isForeground;
        state = CharState.IDLE;

        this.gameManager = gameManager;
    }

    public Action getActions()
    {

        return actions;

    }

    public void update()
    {

        switch (state)
        {

            case IDLE:
                if (actions.getInteractionState(Interaction.PUNCH) &&
                        System.currentTimeMillis() - startTime > punchingCooldown)
                {

                    changeState(CharState.PREPARINGPUNCH);
                    gameManager.playSound("prepPunch");
                    m_anim.playAnim("prepPunch");

                }
                else if (actions.getInteractionState(Interaction.DODGE) &&
                        System.currentTimeMillis() - startTime > dodgingCooldown)
                {

                    changeState(CharState.DODGING);
                    gameManager.playSound("dodge");
                    m_anim.playAnim("dodge");

                }
                else if (actions.getInteractionState(Interaction.FAKE) &&
                        System.currentTimeMillis() - startTime > fakingCooldown)
                {

                    changeState(CharState.FAKING);
                    gameManager.playSound("prepPunch");
                    m_anim.playAnim("prepPunch");


                }
                break;

            case PREPARINGPUNCH:
                if(System.currentTimeMillis() - startTime > preparingPunchTime)
                {
                    changeState(CharState.PUNCHING);
                    gameManager.playSound("punch");
                    m_anim.playAnim("punch");
                }
                break;

            case PUNCHING:
                if(System.currentTimeMillis() - startTime > punchingTime)
                {
                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");
                    punchingCooldown = punchingCooldownConstants;

                }

                punchBehavior();

                break;

            case DODGING:
                if(System.currentTimeMillis() - startTime > dodgingTime)
                {

                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");
                    dodgingCooldown = dodgingCooldownConstants;

                }
                break;

            case FAKING:
                if(System.currentTimeMillis() - startTime > fakingTime)
                {

                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");
                    fakingCooldown = fakingCooldownConstants;

                }
                break;

            case DAMAGED:
                if(System.currentTimeMillis() - startTime > damagedTime)
                {
                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");

                }
                break;

            default:
                break;

        }

        m_anim.update(actions.direction);

    }

    protected void changeState(CharState newState)
    {

        startTime = System.currentTimeMillis();

        state = newState;

        punchingCooldown = 0;
        dodgingCooldown = 0;
        fakingCooldown = 0;

        if(newState == CharState.IDLE) {

            actions.resetInteractionStates();
            actions.resetInteractionDirection();

        }

    }

    protected void punchBehavior()
    {

        gameManager.hit(isPlayer);

    }

    public void receiveDamage(int damage)
    {

        if(!m_isInvincible)
            m_lifePoints -= damage;

        if(m_lifePoints <= 0)
            onDeath();

    }

    protected void onDeath()
    {}


    public int getLifePoints() {
        return m_lifePoints;
    }
    public boolean isInvincible() {
        return m_isInvincible;
    }


    public void setLifePoints(int lifePoints) {
        this.m_lifePoints = lifePoints;
    }
    public void setIsInvincible(boolean isInvincible) {
        this.m_isInvincible = isInvincible;
    }


}
