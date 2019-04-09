package deck;

import java.util.*;

/**
 * WeaponCard is the card which represent a weapon.
 */
public abstract class WeaponCard extends Card {

    private ArrayList<Bullet> cost;
    private ArrayList<Attack> attacks;
    private boolean isLoaded;

    /**
     * Default constructor
     */

    public WeaponCard() {
        this.cost = new ArrayList<>();
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(ArrayList<Bullet> cost, ArrayList<Attack> attacks) {
        this.cost = cost;
        this.attacks = attacks;
        isLoaded = false;
    }

    /**
     * End constructors
     */

    /**
     * This function returns the list of attacks a weapon has
     * @return the list of attacks
     */

    public ArrayList<Attack> getAttacks(){
        return this.attacks;
    }
    /**
     * This function returns the cost of every attacks
     * @return the cost of every attack
     */
    public ArrayList<Bullet> getCost(){
        return this.cost;
    }

    public Attack getAttack(int idAttack) {
        // TODO implement here
        return null;
    }

}