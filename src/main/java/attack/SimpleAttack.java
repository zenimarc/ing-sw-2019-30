package attack;


import player.Player;

public class SimpleAttack extends Attack implements AttackSinglePlayerInterface {

    private static int damage;
    private static int mark;

    public SimpleAttack(String name, String description, int damage, int mark){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public boolean attack(Player player, Player opponents) {
        opponents.addDamage(player,damage);
        opponents.addMark(player, mark);
    return true;
    }

}