package attack;

import board.Cell.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.EnumString;
import controller.EnumTargetSet;
import player.Player;

import java.util.List;

public class MoveAttack extends Attack {

    private int step;

    /**
     * MoveAttack Costructor
     * @param name Attack name
     * @param desc Attack Description
     * @param step Max step
     * @param damage Damage
     * @param target Max Target
     */
    @JsonCreator
    public MoveAttack(@JsonProperty("targetType") EnumTargetSet targetType,
                      @JsonProperty("name") EnumString name,
                      @JsonProperty("description") String desc,
                      @JsonProperty("step") int step,
                      @JsonProperty("damage") int damage,
                      @JsonProperty("target") int target){
        this(targetType,name, step, damage, target);
        this.description = desc;
    }

    /**
     * MoveAttack constructor
     * @param name Attack name
     * @param step Max step
     * @param damage Damage
     * @param target Max target
     */
    public MoveAttack(EnumTargetSet targetType, EnumString name, int step, int damage, int target){
        this.targetType = targetType;
        this.name = name;
        this.step = step;
        this.damage = damage;
        this.description = getDescription();
        this.target = target;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents) {
        return false;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents, Cell newCell) {
        for(Player p : opponents){
            singleAttack(player,p,newCell);
        }
        return true;
    }

    public boolean singleAttack(Player player, Player opponent, Cell newCell){
        opponent.setPawnCell(newCell);
        opponent.addDamage(player,damage);
        return true;
    }

    @Override
    public String getDescription() {
        return "Sposta un avversario di "+step+" caselle.";
    }
}
