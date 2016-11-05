package Game;

/**
 * Created by Maxime on 2016-11-05.
 */

public class GameManager {

    Character player1;
    Character player2;

    public GameManager()
    {

        player1 = new Character(3, true);
        player2 = new Character(3, false);

    }

    public int gameOver()
    {

        return 0;

    }

}
