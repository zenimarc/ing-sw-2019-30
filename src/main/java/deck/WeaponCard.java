package deck;

import java.util.*;

/**
 * ci saranno delle sottoclassi di armi con esempio 2 attacchi, 3 attacchi.
 */
public abstract class WeaponCard extends Card {

    /**
     * Default constructor
     */
    public WeaponCard() {
    }

    /**
     * l'array è di tre elementi, indica in ordine il costo di ogni colore. es. array [2, 0, 1] paghi 2 rossi, 0 gialli e 1 blu.
     */
    private ArrayList<Bullet> cost;

    /**
     * 
     */
    private ArrayList<Attack> attacks;

    /**
     * 
     */
    private boolean isLoaded;


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