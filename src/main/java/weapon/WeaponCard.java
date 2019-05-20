package weapon;

import attack.Attack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import deck.Bullet;
import deck.Card;
import player.Player;

import java.util.*;

/**
 * WeaponCard is the card which represent a weapon.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleWeapon.class),
        @JsonSubTypes.Type(value = MovementWeapon.class),
        @JsonSubTypes.Type(value = DistanceWeapon.class),
        @JsonSubTypes.Type(value = AreaWeapon.class)
})

public abstract class WeaponCard extends Card {
    //TODO se è astratta, non è possibile inizializzare la classe e quindi creare le carte,
    // si può fare anche se è abstract in un altro modo? -Christian

    protected String name;
    protected List<Bullet> cost;

    protected List<Attack> attacks;
    protected boolean isLoaded;

    //Default constructor
    public WeaponCard() {
        this.cost = new ArrayList<>();
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name) {
        this.name = name;
        this.cost = new ArrayList<>();
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name,List<Bullet> cost) {
        this.name = name;
        this.cost = cost;
        this.attacks = new ArrayList<>();
    }

    public WeaponCard(String name, List<Bullet> cost, List<Attack> attacks) {
        this.name = name;
        this.cost = cost;
        this.attacks = attacks;
    }

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
    public List<Bullet> getCost(){
        return this.cost;
    }

    /**
     * Get the weapon name
     * @return weapon name
     */
    public String getName(){return this.name;}

    public boolean isReady(){return this.isLoaded;}

    public Attack getAttack(int idAttack) {
        return attacks.get(idAttack);
    }

    public abstract boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell);

    public boolean shoot(int typeAttack, Player shooter, Player opponent, Optional<Cell> cell){
        return shoot(typeAttack, shooter, new ArrayList<>(Arrays.asList(opponent)), cell);
    }


    protected boolean alternativeSimpleShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack) {
            case 0:
                attacks.get(0).attack(shooter, opponents);
                break;
            case 1:
                attacks.get(1).attack(shooter, opponents);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}