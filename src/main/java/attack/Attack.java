package attack;

import board.Cell;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * This class is an idea of genertic attack.
 * Need implements different attack's type.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = DistanceAttack.class),
        @JsonSubTypes.Type(value = MoveAttack.class),
        @JsonSubTypes.Type(value = SimpleAttack.class)
})

public abstract class Attack {

    protected EnumAttackName name;
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
    public EnumAttackName getName() {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name.getString());
        sb.append(": ");
        sb.append(this.getDescription());

        return sb.toString();
    }
}