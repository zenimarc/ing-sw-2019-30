package deck;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

import static deck.Bullet.intArrayToString;

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
        hasPowerUp = false;
    }

    @JsonCreator
    public AmmoCard(@JsonProperty("cubes") int[] cubes,
                    @JsonProperty("hasPowerUp") boolean hasPowerUp) {
        this.cubes = cubes;
        this.hasPowerUp = hasPowerUp;
    }

    /**
     * This function returns an array containing the ammo cubes the AmmoCard has
     * @return an array of cubes
     */
    public int[] getAmmo(){return this.cubes;}

    /**
     * This function returns the number of cubes of a specific color
     * @param i number used to choose the color
     * @return the number of cubes of a specific color
     */
    public int getSpecificAmmo(int i){return this.cubes[i];}

    /**
     * This function verifies if the AmmoCard can give a power up
     * @return true if it can, else false
     */

    public boolean verifyPowerUp(){return this.hasPowerUp;}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(intArrayToString(cubes));

        if(hasPowerUp)
            stringBuilder.append("+Power");
        else stringBuilder.append("      ");

        return stringBuilder.toString();
    }

    @Override
    public String stringGUI(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < 3; i++)
            stringBuilder.append(cubes[i]);

        if(hasPowerUp)
            stringBuilder.append("y");
        else stringBuilder.append("n");

        return stringBuilder.toString();
    }
}