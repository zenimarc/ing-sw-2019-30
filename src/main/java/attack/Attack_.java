package attack;

import deck.Color;
import player.Player;

import java.util.ArrayList;

public abstract class Attack_ {

    protected static String name;
    protected static String description;
    protected static int[] bulletsColor;

    public abstract boolean attack(Player player, Player opponent);
    public abstract String getDescription();

    public void setCost(int[] bulletsColor) {
        this.bulletsColor = bulletsColor;
    }

    public static int[] getCost() {
        return bulletsColor;
    }

    public String getName(){return this.name;}

    }
