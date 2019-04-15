package attack;

import player.Player;

import java.util.ArrayList;

/**
 * Single attack is an attack that hit an opponent and add damage and/or mark to it
 */
public class SimpleAttack extends Attack_{

    private int target;
    private int damage;
    private int mark;

    //Constructor
    public SimpleAttack(String name, String description, int damage, int mark, int target){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;
        this.target = target;

    }

    public SimpleAttack(String name, int damage, int mark, int target){
        this.name =  name;
        this.damage = damage;
        this.mark = mark;
        this.target = target;
    }

    public SimpleAttack(String name, int damage, int mark){
        this(name, damage, mark, 1);
    }

    //End Constructor

    /**
     * Add damage and mark to opponent by player
     * @param player Player that attack
     * @param opponents Player hit
     * @return true
     */
    @Override
    public boolean attack(Player player, ArrayList<Player> opponents) {
        if(opponents.size()==target) {
            for (Player opponent : opponents) {
                attack(player, opponent);
            }
            return true;
        }
        return false;
    }

    public boolean attack(Player player, Player opponent) {
        opponent.addDamage(player,damage);
        opponent.addMark(player, mark);
        return true;
    }


    /**
     * This create an attack's standard description
     * @return
     */
    public String stdDescription(){
        StringBuilder sb = new StringBuilder();
        sb.append("Dai ");
        sb.append(damage);
        sb.append(" danni e ");
        sb.append(mark);
        sb.append(" marchio/i a 1 bersaglio che puoi vedere.");
        return sb.toString();
    }


    /**
     * This return attack's description
     * @return attack's description
     */
    @Override
    public String getDescription() {
        if(description==null) description=stdDescription();
        return this.description;
    }

    /**
     * Get attaks's damages
     * @return attaks's damages
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get attak's marks
     * @return attak's marks
     */
    public int getMark() {
        return mark;
    }
}