package attack;

import board.Cell;
import constants.EnumString;
import player.Player;

import java.util.List;

public class MoveAttack extends Attack {

    private int step;

    /**
     *
     * @param name Attack name
     * @param desc Attack Description
     * @param step Max step
     * @param damage Damage
     * @param target Max Target
     */
    public MoveAttack(EnumString name, String desc, int step, int damage, int target){
        this.name = name;
        this.description = desc;
        this.step = step;
        this.damage = damage;
        this.target = target;
    }

    //Costructor
    public MoveAttack(EnumString name, int step, int damage, int target){
        this.name = name;
        this.step = step;
        this.damage = damage;
        this.description = getDescription();
        this.target = target;
    }

    //Costructor
    public MoveAttack(EnumString name, int step, int damage){
        this(name,step, damage, 1);
        this.description = getDescription();
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
        opponent.setCell(newCell);
        opponent.addDamage(player,damage);
        return true;
    }

    @Override
    public String getDescription() {
        return "Sposta un avversario di "+step+" caselle.";
    }
}
