package attack;

import board.Cell;
import constants.EnumString;
import player.Player;

import java.util.List;
/**
 * Single attack is an attack that hit an opponent and add damage and/or mark to it
 *
 * target = -1 => no-defined num of target
 */
public class SimpleAttack extends Attack {

    //Constructor
    public SimpleAttack(EnumString name, String description, int damage, int mark, int target){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;
        this.target = target;

    }

    public SimpleAttack(EnumString name, int damage, int mark, int target){
        this.name =  name;
        this.damage = damage;
        this.mark = mark;
        this.target = target;
    }

    public SimpleAttack(EnumString name, int damage, int mark){
        this(name, damage, mark, 1);
    }

    //End Constructor

    /**
     * Add damage and mark to opponent by player
     * @param player Player that attack
     * @param opponents Player hit
     * @param newCell not used in this attack type
     * @return true
     */
    @Override
    public boolean attack(Player player, List<Player> opponents, Cell newCell) {
        return attack(player,opponents);
    }

    /**
     * Add damage and mark to opponents list by player
     * @param player Player who attack
     * @param opponents Players hit
     * @return true
     */
    @Override
    public boolean attack(Player player, List<Player> opponents) {

        if(opponents.size()>target && target!=-1){
            opponents = opponents.subList(0,target);
        }

            for (Player opponent : opponents) {
                singleAttack(player, opponent);
            }
            return true;
    }

    /**
     * Add damage and mark to single opponent by player
     * @param player Player who attack
     * @param opponent Player hit
     * @return true
     */

    public boolean singleAttack(Player player, Player opponent) {
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