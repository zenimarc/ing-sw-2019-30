package attack;

import board.Cell;
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
    public DistanceAttack(EnumTargetSet targetType, EnumString name, int damage, int mark, int target, int minDistance, int maxDistance){
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
