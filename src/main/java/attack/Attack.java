package attack;

import board.Cell;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class is an idea of genertic attack.
 * Need implements different attack's type.
 */
public abstract class Attack {

    protected String name;
    protected String description;
    protected int[] bulletsColor;
    protected int target;

    public abstract boolean attack(Player player, List<Player> opponents, Cell newCell);

    public abstract boolean attack(Player player, List<Player> opponents);

    public boolean attack(Player player, Player opponent, Cell newCell){
        return attack(player,new ArrayList<>(Arrays.asList(opponent)),newCell);
    }

    public boolean attack(Player player, Player opponent){
        return attack(player,new ArrayList<>(Arrays.asList(opponent)));
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
