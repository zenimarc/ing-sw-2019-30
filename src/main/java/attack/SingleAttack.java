package attack;

import player.Player;

/**
 * Single attack is an attack that hit an opponent and add damage and/or mark to it
 */
public class SingleAttack extends Attack_{

    private int damage;
    private int mark;

    //Constructor
    public SingleAttack(String name, String description, int damage, int mark){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;

    }

    public SingleAttack(String name, int damage, int mark){
        this.name =  name;
        this.damage = damage;
        this.mark = mark;
    }
    //End Constructor

    /**
     * Add damage and mark to opponent by player
     * @param player Player that attack
     * @param opponent Player hit
     * @return true
     */
    @Override
    public boolean attack(Player player, Player opponent) {
        opponent.addDamage(player,damage);
        opponent.addMark(player, mark);
    return true;
    }

    /**
     * This return attack's description
     * @return attack's description
     */
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dai ");
        sb.append(damage);
        sb.append(" danni e ");
        sb.append(mark);
        sb.append(" marchio/i a 1 bersaglio che puoi vedere.");
        return sb.toString();
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