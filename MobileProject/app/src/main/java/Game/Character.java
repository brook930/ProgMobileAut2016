package Game;

/**
 * Created by Maxime on 2016-11-05.
 */

public class Character {

    private int m_lifePoints;

    private boolean m_isInvincible;

    public Anim m_anim;

    public Character(int lifePoints, boolean isForeground)
    {

        m_lifePoints = lifePoints;
        m_anim = new Anim(isForeground);

        m_isInvincible = false;

    }

    public void update()
    {

        m_anim.update();

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
