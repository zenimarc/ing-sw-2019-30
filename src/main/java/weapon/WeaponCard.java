package weapon;

import attack.Attack_;
import deck.Card;
import player.Player;

import java.util.*;

/**
 * WeaponCard is the card which represent a weapon.
 */
public abstract class WeaponCard extends Card {

    private String name;
    private int[] cost;
    protected List<Attack_> attacks;
    private boolean isLoaded;

    /**
     * Default constructor
     */

    public WeaponCard() {
        this.cost = new int[3];
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name, int[] cost, List<Attack_> attacks) {
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

    public List<Attack_> getAttacks(){
        return this.attacks;
    }
    /**
     * This function returns the cost of every attack
     * @return the cost of every attack
     */
    public int[] getCost(){
        return this.cost;
    }

    public Attack_ getAttack(int idAttack) {
        return attacks.get(idAttack);
    }

    public abstract boolean shoot (Player shooter, Player opposit, int idAttack);
}