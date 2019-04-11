package weapon;

import attack.Attack;
import deck.Card;

import java.util.*;

/**
 * WeaponCard is the card which represent a weapon.
 */
public abstract class WeaponCard extends Card {

    private String name;
    private int[] cost;
    private List<Attack> attacks;
    private boolean isLoaded;

    /**
     * Default constructor
     */

    public WeaponCard() {
        this.cost = new int[3];
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name, int[] cost, List<Attack> attacks) {
        this.name = name;
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

    public List<Attack> getAttacks(){
        return this.attacks;
    }
    /**
     * This function returns the cost of every attack
     * @return the cost of every attack
     */
    public int[] getCost(){
        return this.cost;
    }

    public Attack getAttack(int idAttack) {
        return attacks.get(idAttack);
    }

}