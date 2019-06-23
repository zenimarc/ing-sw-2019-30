package attack;

import board.Cell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import player.Player;

import java.util.List;

public class PriorityAttack extends Attack{
        private int priority;
        private int movement;

    /**
     *
     * @param targetType
     * @param name
     * @param damage
     * @param target
     * @param movement
     * @param priority
     */
        @JsonCreator
        public PriorityAttack(@JsonProperty("targetType") EnumTargetSet targetType,
                              @JsonProperty("name") EnumAttackName name,
                              @JsonProperty("damage") int damage,
                              @JsonProperty("target") int target,
                              @JsonProperty("movement") int movement,
                              @JsonProperty("priority") int priority)
            {
            this.targetType = targetType;
            this.name = name;
            this.damage = damage;
            this.movement = movement;
            this.priority = priority;
            this.description = this.name.getDescription();
            this.target = target;
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

    public int getMovement() {
        return movement;
    }
    public int getPriority(){return priority;}
}

