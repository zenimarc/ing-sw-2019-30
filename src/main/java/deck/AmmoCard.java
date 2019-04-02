package deck;

import java.util.*;

/**
 * 
 */
public class AmmoCard extends Card {
    private int[] cubes;
    private boolean hasPowerUp;


/** TODO
    public AmmoCard() {
        this.cubes = [0,0,0];
    }
**/
    public AmmoCard(int[] cubes, boolean hasPowerUp) {
        this.cubes = cubes;
        this.hasPowerUp = hasPowerUp;
    }

}