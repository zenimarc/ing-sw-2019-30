package attack;

import board.Cell;
import constants.EnumString;
import player.Player;

import java.util.List;

public class DistanceAttack extends Attack {

    private int minDistance;
    private int maxDistance;


    //Costructor
    public DistanceAttack(EnumString name, int damage, int mark, int target,int minDistance, int maxDistance){
        this.name = name;
        this.damage = damage;
        this.mark = mark;
        this.description = getDescription();
        this.target = target;
    }

    //Costructor
    public DistanceAttack(EnumString name, String desc, int damage, int mark, int target, int minDistance, int maxDistance){
        this(name, damage, mark, target,minDistance, maxDistance);
        this.description = desc;
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
        return false;
    }

    @Override
    public String getDescription() {
        return minDistance==maxDistance ? "Attacca un avversario che si trova a "+ minDistance+" step." :
                "Attaca un avversario che si trova a minimo "+minDistance+" step e maximo "+maxDistance+" step.";
    }
}
