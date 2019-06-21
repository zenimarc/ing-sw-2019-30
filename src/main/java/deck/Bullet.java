package deck;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * TODO Bullet is used for graphics
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
     * This function converts an array of cubes into a String
     * @param cubes with info
     * @return a string
     */
    public static String intArrayToString(int[] cubes){

        if(cubes==null) return "";
        else return "[R:"+cubes[0]+",Y:"+cubes[1]+",B:"+cubes[2]+"]";
    }

    public static String mapToString(Map<Color, Integer> map){

        int[] bullets = new  int[]{map.get(Color.RED), map.get(Color.YELLOW),map.get(Color.BLUE)};

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