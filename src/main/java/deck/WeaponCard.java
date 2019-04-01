package deck;

import java.util.*;

/**
 * ci saranno delle sottoclassi di armi con esempio 2 attacchi, 3 attacchi.
 */
public abstract class WeaponCard extends Card {


    private ArrayList<Bullet> cost;
    private ArrayList<Attack> attacks;
    private boolean isLoaded;

    public WeaponCard() {
    }



    /**
     * @return
     */
    public Bullet getFirst() {
        // TODO implement here
        return null;
    }

    /**
     *
     * @return
     */
    public Attack getAttack(int idAttack) {
        // TODO implement here
        return null;
    }

}