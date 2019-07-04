package attack;

import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import player.Player;

import java.util.List;
/**
 * Single attack is an attack that hit an opponent and add damage and/or mark to it
 *
 * target = -1 => no-defined num of target
 */
public class SimpleAttack extends Attack {

    public SimpleAttack(EnumTargetSet targetType, EnumAttackName name, int damage, int mark, int target) {
        this.targetType = targetType;
        this.name = name;
        this.damage = damage;
        this.mark = mark;
        this.target = target;
        this.description = this.name.getDescription();
    }

    @JsonCreator
    public SimpleAttack(@JsonProperty("targetType") EnumTargetSet targetType,
                        @JsonProperty("name") EnumAttackName name,
                        @JsonProperty("description") String description,
                        @JsonProperty("damage") int damage,
                        @JsonProperty("mark") int mark,
                        @JsonProperty("target") int target) {
        this(targetType, name, damage, mark, target);
        this.description = description;
    }

    //End Constructor

    /**
     * Add damage and mark to opponent by player
     *
     * @param player    Player that attack
     * @param opponents Player hit
     * @param newCell   not used in this attack type
     * @return true
     */
    @Override
    public boolean attack(Player player, List<Player> opponents, Cell newCell) {
        return attack(player, opponents);
    }

    /**
     * Add damage and mark to opponents list by player
     *
     * @param player    Player who attack
     * @param opponents Players hit
     * @return true
     */
    @Override
    public boolean attack(Player player, List<Player> opponents) {
        for (Player opponent : opponents) {
            if (opponent != null) {
                opponent.addDamage(player, damage);
                opponent.addMark(player, mark);
            }
        }
        return true;
    }

    @Override
    public boolean attack(Cell cell) {
        return false;
    }

}