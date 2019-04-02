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
        this(null, null);
    }

    public WeaponCard(ArrayList<Bullet> cost, ArrayList<Attack> attacks) {
        cost = new ArrayList<Bullet>();
        attacks = new ArrayList<Attack>();
        isLoaded = false;
    }

    public ArrayList<Attack> getAttacks(){
        return this.attacks;
    }
    /**
     *
     * @return
     */
    public ArrayList<Bullet> getCost(){
        return this.cost;
    }

    public Attack getAttack(int idAttack) {
        // TODO implement here
        return null;
    }

}