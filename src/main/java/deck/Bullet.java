package deck;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

import static constants.Color.*;

/**
 * Bullet is a class used to represent ammo
 */
public class Bullet implements Serializable {

    private Color color;

    /**
     * Constructors
     */

    @JsonCreator
    public Bullet(@JsonProperty("cost") Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


    /**
     * This function converts an ArrayList of Bullet int an int array[num of R,num of Y, num of B]
     * @param bullets to be converted
     * @return an array
     */
    @Contract(pure = true)
    public static int[] toIntArray(@NotNull List<Bullet> bullets){
        int[] colorArray = new int[]{0,0,0};
        for(Bullet bullet : bullets){
            switch (bullet.color){
                case RED:
                    colorArray[0]++;
                    break;
                case YELLOW:
                    colorArray[1]++;
                    break;
                case BLUE:
                    colorArray[2]++;
                    break;
            }
        }
        return colorArray;
    }

    /**
     * This function converts an ArrayList of Bullet into an int array[num of R,num of Y, num of B]
     * @param bullets to be converted
     * @return an array
     */
    @Contract(pure = true)
    public static int[] mapToIntArray(@NotNull Map<Color, Integer> bullets){
        int[] colorArray = new int[]{0,0,0};

        if(bullets.containsKey(RED))
            colorArray[0] = bullets.get(RED);
        if(bullets.containsKey(YELLOW))
            colorArray[1] = bullets.get(YELLOW);
        if(bullets.containsKey(BLUE))
            colorArray[2] = bullets.get(BLUE);
        return colorArray;
    }

    @NotNull
    @Contract(pure = true)
    public static int[] colorToArray(Color color){
        switch (color) {
            case RED:
                return new int[]{1, 0, 0};
            case YELLOW:
                return new int[]{0, 1, 0};
            case BLUE:
                return new int[]{0, 0, 1};
            default:
                return new int[]{0, 0, 0};
        }
    }

    /**
     * This function converts an array of cubes into a String
     * @param cubes with info
     * @return a string
     */
    public static String intArrayToString(int[] cubes){

        if(cubes==null) return "";
        else return "[R:"+cubes[0]+",Y:"+cubes[1]+",B:"+cubes[2]+"]";
    }

    /**
     * This function converts a Map of bullets to an array which then is converted into a string for CLI
     * @param map to be converted
     * @return a string
     */
    public static String mapToString(Map<Color, Integer> map){

        int[] bullets = new  int[]{map.get(RED), map.get(Color.YELLOW),map.get(Color.BLUE)};

        return intArrayToString(bullets);
    }

    /**
     * This function is used for searching a specific AmmoCard for GUI
     * @return a string
     */
    public String stringGUI() {
        return color.getFirstLetter();
    }
}