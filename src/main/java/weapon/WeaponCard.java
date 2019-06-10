package weapon;

import attack.Attack;
import board.Cell;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import deck.Bullet;
import deck.Card;
import player.Player;

import java.util.*;

import static deck.Bullet.intArrayToString;
import static deck.Bullet.toIntArray;


/**
 * WeaponCard is the card which represent a weapon.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        property = "WeaponClass")

@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleWeapon.class),
        @JsonSubTypes.Type(value = MovementWeapon.class),
        @JsonSubTypes.Type(value = DistanceWeapon.class),
        @JsonSubTypes.Type(value = AreaWeapon.class)
})

public abstract class WeaponCard extends Card {

    protected String name;
    protected List<Bullet> cost;
    protected EnumWeapon weaponType;

    protected List<Attack> attacks;
    protected Attack alternativeAttack;
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
        this(name, cost);
        this.attacks = attacks;
    }
    
    public WeaponCard(String name, List<Bullet> cost, Attack alternativeAttack){
        this(name, cost);
        this.alternativeAttack = alternativeAttack;
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
     * Return grab cost of this weapon
     * @return cost
     */
    @JsonIgnore
    public List<Bullet> getGrabCost(){
        if (this.cost.size() > 1) {
            return this.cost.subList(1,this.cost.size());
        }else{
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Get the weapon name
     * @return weapon name
     */
    public String getName(){return this.name;}


    @JsonIgnore
    public boolean isReady(){return this.isLoaded;}

    /**
     * Getter, return the attack in position idAttack
     * @param idAttack attack's index
     * @return attack in position idAttack
     */
    public Attack getAttack(int idAttack) {
        return attacks.get(idAttack);
    }

    /**
     * Attack all player in @param opponents by the attack in position typeAttack
     * @param typeAttack attack's index
     * @param shooter Player who shoot
     * @param opponents Player hit
     * @param cell
     * @return
     */
    public abstract boolean shoot(int typeAttack, Player shooter, List<Player> opponents, Optional<Cell> cell);

    public boolean shoot(int typeAttack, Player shooter, Player opponent, Optional<Cell> cell){
        return shoot(typeAttack, shooter, new ArrayList<>(Arrays.asList(opponent)), cell);
    }

    /**
     * Get WeaponType of this weapon
     * @return Weapontype of this weapon
     */
    public EnumWeapon getWeaponType() {
        return weaponType;
    }

    protected boolean alternativeSimpleShoot(int typeAttack, Player shooter, List<Player> opponents){
        switch (typeAttack) {
            case 0:
                attacks.get(0).attack(shooter, opponents);
                break;
            case 1:
                alternativeAttack.attack(shooter, opponents);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getGrabCostCLI(){
        return intArrayToString(toIntArray(getGrabCost()));
    }

    public String getWeaponCostCLI(){
        return intArrayToString(toIntArray(cost));
    }

    public String isLoadedCLI(){
        if(isLoaded)
            return "Loaded    ";
        else return "Not loaded";
    }

    public void setLoaded() {
        isLoaded = true;
    }

    public void  setNotLoaded(){isLoaded = false;}

    @Override
    public String stringGUI(){
        return this.name;
    }
}