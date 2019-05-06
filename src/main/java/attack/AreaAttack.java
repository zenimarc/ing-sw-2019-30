package attack;

import board.Cell;
import constants.EnumString;
import player.Player;

import java.util.List;

public class AreaAttack extends Attack {

    //Costructor
    public AreaAttack(EnumString name, String desc, int damage, int target){
        this.name = name;
        this.description = desc;
        this.damage = damage;
        this.target = target;
    }

    //Costructor
    public AreaAttack(EnumString name, int damage, int target){
        this.name = name;
        this.damage = damage;
        this.description = getDescription();
        this.target = target;
    }

    //Costructor
    public AreaAttack(EnumString name, int damage){
        this(name, damage, 1);
        this.description = getDescription();
    }

    @Override
    public boolean attack(Player player, List<Player> opponents, Cell newCell) {
        return false;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
