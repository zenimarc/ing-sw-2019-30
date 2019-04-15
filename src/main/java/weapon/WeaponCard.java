package weapon;

import attack.Attack_;
import deck.Bullet;
import deck.Card;
import player.Player;

import java.util.*;

/**
 * WeaponCard is the card which represent a weapon.
 */
public abstract class WeaponCard extends Card {

    protected String name;
    private List<Bullet> cost;
    protected List<Attack_> attacks;
    protected boolean isLoaded;
    protected enumWeapon weaponType;


    //Default constructor
    public WeaponCard() {
        this.cost = new ArrayList<>();
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name,List<Bullet> cost) {
        this.name = name;
        this.cost = cost;
        this.attacks = new ArrayList<>();
        isLoaded = false;
    }

    public WeaponCard(String name, List<Bullet> cost, List<Attack_> attacks) {
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
        isLoaded = false;
    }

    //End constructors


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
    public List<Bullet> getCost(){
        return this.cost;
    }

    /**
     * Get the weapon name
     * @return weapon name
     */
    public String getName(){return this.name;}

    public Attack_ getAttack(int idAttack) {
        return attacks.get(idAttack);
    }

    public abstract boolean shoot(int idAttack, Player shooter, List<Player> opponets);

    public boolean shoot(int idAttack, Player shooter, Player opponent){
        return shoot(idAttack, shooter, new ArrayList<>(Arrays.asList(opponent)));
    }


}