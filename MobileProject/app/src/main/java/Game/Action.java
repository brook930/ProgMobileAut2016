package Game;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Maxime on 2016-11-05.
 */

enum Interaction
{

    PUNCH,
    DODGE,
    FAKE

}

enum Direction
{

    RIGHT,
    LEFT,
    NONE

}

public class Action {

    public Hashtable<Interaction, Boolean> interactions;
    public Direction direction;

    public Action()
    {

        initInteractions();

        direction = Direction.NONE;

    }

    private void initInteractions()
    {

        interactions = new Hashtable<Interaction, Boolean>();

        interactions.put(Interaction.PUNCH, false);
        interactions.put(Interaction.DODGE, false);
        interactions.put(Interaction.FAKE, false);

    }

    public boolean getInteractionState(Interaction interaction)
    {

        return interactions.get(interaction);

    }

    public Direction getDirection()
    {

        return direction;

    }

    public void setInteractionState(Interaction interaction, boolean isPressed)
    {

        interactions.put(interaction, isPressed);

    }

    public void setInteractionDirection(Direction direction)
    {

        if(this.direction == Direction.NONE)
            this.direction = direction;

    }

    public void reset()
    {

        resetInteractionStates();

        resetInteractionDirection();

    }

    public void resetInteractionStates()
    {

        initInteractions();

    }

    public void resetInteractionDirection()
    {

        direction = Direction.NONE;

    }

}
