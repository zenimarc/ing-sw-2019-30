package attack;

import board.Cell;
import constants.EnumString;
import controller.EnumTargetSet;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is an idea of genertic attack.
 * Need implements different attack's type.
 */
public abstract class Attack {

    protected EnumString name;
    protected int damage;
    protected int mark;
    protected String description;
    protected int[] bulletsColor;
    protected int target;
    protected EnumTargetSet targetType;

    public abstract boolean attack(Player player, List<Player> opponents, Cell newCell);

    public abstract boolean attack(Player player, List<Player> opponents);

    public boolean attack(Player player, Player opponent, Cell newCell){
        return attack(player,new ArrayList<>(Arrays.asList(opponent)),newCell);
    }

    public boolean attack(Player player, Player opponent){
        return attack(player,new ArrayList<>(Arrays.asList(opponent)));
    }

    /**
     * This return the attack's description
     * @return attack's description
     */
    public abstract String getDescription();

    /**
     * set how many bullets need to use this attack
     * @param bulletsColor int[num of R, num of Y, num of B]
     */
    public void setCost(int[] bulletsColor) {
        this.bulletsColor = bulletsColor;
    }

    /**
     * Get how many bullets need to use this attack
     * @return int[num of R, num of Y, num of B]
     */
    public int[] getCost() {
        return bulletsColor;
    }

    /**
     * Get attack's name
     * @return attack's name
     */
    public EnumString getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getMark() {
        return mark;
    }

    public int getTarget() {
        return target;
    }

    public EnumTargetSet getTargetType() {
        return targetType;
    }
    }