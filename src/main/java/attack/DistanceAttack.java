package attack;

import board.Cell.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.EnumString;
import controller.EnumTargetSet;
import player.Player;

import java.util.List;

public class DistanceAttack extends Attack {

    private int minDistance;
    private int maxDistance;


    /**
     *
     * @param name
     * @param damage
     * @param mark
     * @param target
     * @param minDistance
     * @param maxDistance
     */
    @JsonCreator
    public DistanceAttack(@JsonProperty("targetType") EnumTargetSet targetType,
                          @JsonProperty("name") EnumString name,
                          @JsonProperty("damage") int damage,
                          @JsonProperty("mark") int mark,
                          @JsonProperty("target") int target,
                          @JsonProperty("minDistance") int minDistance,
                          @JsonProperty("maxDistance") int maxDistance){
        this.targetType = targetType;
        this.name = name;
        this.damage = damage;
        this.mark = mark;
        this.description = getDescription();
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
        for(Player opponent : opponents){
            opponent.addDamage(player, damage);
        }
        return true;
    }

    @Override
    public String getDescription() {
        return minDistance==maxDistance ? "Attacca un avversario che si trova a "+ minDistance+" step." :
                "Attaca un avversario che si trova a minimo "+minDistance+" step e maximo "+maxDistance+" step.";
    }
}
