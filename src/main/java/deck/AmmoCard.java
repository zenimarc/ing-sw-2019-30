package deck;

import java.util.*;

/**
 * AmmoCard are cards found in a NormalCell
 */
public class AmmoCard extends Card {
    private int[] cubes; //Colors in order are: red, yellow, blue
    private boolean hasPowerUp;

    /**
     * Constructors
     */

    public AmmoCard() {
        this.cubes = new int[]{0, 0, 0};
    }

    public AmmoCard(int[] cubes, boolean hasPowerUp) {
        this.cubes = cubes;
        this.hasPowerUp = hasPowerUp;
    }

    /**
     * This function returns an array containing the ammo cubes the AmmoCard has
     * @return an array of cubes
     */
    public int[] getAmmo(){return this.cubes;}

    /**
     * This function verifies if the AmmoCard can give a power up
     * @return true if it can, else false
     */

    public boolean verifyPowerUp(){return this.hasPowerUp;}
}