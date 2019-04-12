package attack;


import player.Player;

public class SingleAttack extends Attack_{

    private static int damage;
    private static int mark;

    public SingleAttack(String name, String description, int damage, int mark){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;
    }

    public SingleAttack(String name, int damage, int mark){
        this.name = name;
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public boolean attack(Player player, Player opponent) {
        opponent.addDamage(player,damage);
        opponent.addMark(player, mark);
    return true;
    }

    @Override
    public String getDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("Dai ");
        sb.append(damage);
        sb.append(" danni e ");
        sb.append(mark);
        sb.append(" marchio/i a 1 bersagli che puoi vedere.");
        return sb.toString();
    }
}