package attack;

import board.Cell;
import player.Player;

import java.util.List;

public class MoveAttack extends Attack_{

    private int step;
    private int damage;

    //Costructor
    public MoveAttack(String name, String desc, int step, int damage, int target){
        this.name = name;
        this.description = desc;
        this.step = step;
        this.damage = damage;
        this.target = target;
    }

    //Costructor
    public MoveAttack(String name, int step, int damage, int target){
        this.name = name;
        this.step = step;
        this.damage = damage;
        this.description = getDescription();
        this.target = target;
    }

    //Costructor
    public MoveAttack(String name, int step, int damage){
        this(name,step, damage, 1);
        this.description = getDescription();
    }

    @Override
    public boolean attack(Player player, Player opponent, Cell cell){
        opponent.setCell(cell);
        opponent.addDamage(player,damage);
        return true;
    }

    @Override
    public boolean attack(Player player, List<Player> opponents, Cell cell) {
        for(Player p : opponents){
            attack(player,p,cell);
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Sposta un avversario di "+step+" caselle.";
    }
}
