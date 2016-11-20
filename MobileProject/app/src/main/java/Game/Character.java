package Game;

/**
 * Created by Maxime on 2016-11-05.
 */

enum CharState {

    IDLE,
    PUNCHING,
    DODGING,
    FAKING

};

public class Character {

    private int m_lifePoints;

    private boolean m_isInvincible;

    public Action actions;
    public Anim m_anim;

    long punchingTime = 300;
    long dodgingTime;
    long fakingTime;

    long startTime = 0;

    CharState state;

    public Character(int lifePoints, boolean isForeground)
    {

        m_lifePoints = lifePoints;
        m_anim = new Anim(isForeground);
        actions = new Action();

        m_isInvincible = false;

        state = CharState.IDLE;

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

                    changeState(CharState.DODGING);

                }
                break;

            case PUNCHING:
                if(System.currentTimeMillis() - startTime > punchingTime)
                {

                    changeState(CharState.IDLE);

                    m_anim.playAnim("idle");

                }
                break;

            case DODGING:
                if(System.currentTimeMillis() - startTime > dodgingTime)
                {

                    changeState(CharState.DODGING);

                }
                break;

            case FAKING:
                if(System.currentTimeMillis() - startTime > fakingTime)
                {

                    changeState(CharState.FAKING);

                }
                break;

            default:
                break;

        }

        m_anim.update();

    }

    private void changeState(CharState newState)
    {

        startTime = System.currentTimeMillis();

        state = newState;

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
