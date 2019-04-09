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
     * End constructors
     */
}