package attack;

import deck.Color;
import player.Player;

public abstract class Attack_ {

    protected static String name;
    protected static String description;
    protected static Color bulletColor;

    public abstract boolean attack(Player player, Player opponent);
    public abstract String getDescription();

    public String getName(){return this.name;}


    }
