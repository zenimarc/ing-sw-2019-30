package attack;

import player.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is an idea of genertic attack.
 * Need implements different attack's type.
 */
public abstract class Attack_ {

    protected String name;
    protected String description;
    protected int[] bulletsColor;

    public abstract boolean attack(Player player, ArrayList<Player> opponents);

    public boolean attack(Player player, Player opponents){
        return attack(player, new ArrayList<>(Arrays.asList(opponents)));
    }


    /**
     * This return the attack's description
     * @return attack's description
     */
    public abstract String getDescription();

    /**
     * set how many bullets need to use this attack
     * @param bulletsColor int[num of R, num of Y, num of B]
     */
    public void setCost(int[] bulletsColor) {
        this.bulletsColor = bulletsColor;
    }

    /**
     * Get how many bullets need to use this attack
     * @return int[num of R, num of Y, num of B]
     */
    public int[] getCost() {
        return bulletsColor;
    }

    /**
     * Get attack's name
     * @return attack's name
     */
    public String getName(){return this.name;}

    }
