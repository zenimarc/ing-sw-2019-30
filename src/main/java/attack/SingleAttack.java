package attack;


import player.Player;

public class SingleAttack extends Attack_ implements AttackSinglePlayerInterface {

    private static int damage;
    private static int mark;

    public SingleAttack(String name, String description, int damage, int mark){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public boolean attack(Player player, Player opponent) {
        opponent.addDamage(player,damage);
        opponent.addMark(player, mark);
    return true;
    }

}