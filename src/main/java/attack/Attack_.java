package attack;

import player.Player;

public abstract class Attack_ {

    protected static String name;
    protected static String description;

    public abstract boolean attack(Player player, Player opponent);


    }
