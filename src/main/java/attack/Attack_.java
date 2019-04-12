package attack;

import player.Player;

public abstract class Attack_ {

    protected String name;
    protected String description;
    protected int[] bulletsColor;

    public abstract boolean attack(Player player, Player opponent);
    public abstract String getDescription();

    public void setCost(int[] bulletsColor) {
        this.bulletsColor = bulletsColor;
    }

    public int[] getCost() {
        return bulletsColor;
    }

    public String getName(){return this.name;}

    }
