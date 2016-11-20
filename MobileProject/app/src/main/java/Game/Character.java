package Game;

/**
 * Created by Maxime on 2016-11-05.
 */

enum CharState {

    IDLE,
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

    long punchingTime = 300;
    long dodgingTime = 300;
    long fakingTime = 300;
    long damagedTime = 300;

    long startTime = 0;

    GameManager gameManager;

    CharState state;

    public Character(int lifePoints, boolean isForeground, GameManager gameManager)
    {

        m_lifePoints = lifePoints;
        m_anim = new Anim(isForeground);
        actions = new Action();

        m_isInvincible = false;

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
                if (actions.getInteractionState(Interaction.PUNCH))
                {

                    changeState(CharState.PUNCHING);

                    m_anim.playAnim("punch");

                }
                else if (actions.getInteractionState(Interaction.DODGE))
                {

                    changeState(CharState.DODGING);

                }
                else if (actions.getInteractionState(Interaction.FAKE))
                {

                    changeState(CharState.FAKING);

                }
                break;

            case PUNCHING:
                if(System.currentTimeMillis() - startTime > punchingTime)
                {

                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");

                }

                punchBehavior();

                break;

            case DODGING:
                if(System.currentTimeMillis() - startTime > dodgingTime)
                {

                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");

                }
                break;

            case FAKING:
                if(System.currentTimeMillis() - startTime > fakingTime)
                {

                    changeState(CharState.IDLE);
                    m_anim.playAnim("idle");

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

    private void changeState(CharState newState)
    {

        startTime = System.currentTimeMillis();

        state = newState;

    }

    private void punchBehavior()
    {

        gameManager.hit();

    }

    public void receiveDamage(int damage)
    {

        if(!m_isInvincible)
            m_lifePoints -= damage;

        if(m_lifePoints <= 0)
            onDeath();

    }

    private void onDeath()
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
