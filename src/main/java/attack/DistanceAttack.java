package attack;

import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import player.Player;

import java.util.List;

public class DistanceAttack extends Attack {

    private int minDistance;
    private int maxDistance;

    @JsonCreator
    public DistanceAttack(@JsonProperty("targetType") EnumTargetSet targetType,
                          @JsonProperty("name") EnumAttackName name,
                          @JsonProperty("damage") int damage,
                          @JsonProperty("mark") int mark,
                          @JsonProperty("target") int target,
                          @JsonProperty("minDistance") int minDistance,
                          @JsonProperty("maxDistance") int maxDistance){
        this.targetType = targetType;
        this.name = name;
        this.damage = damage;
        this.mark = mark;
        this.description = this.name.getDescription();
        this.target = target;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents, Cell newCell) {
        return false;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents) {
        for(Player opponent : opponents) {
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

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }
}
