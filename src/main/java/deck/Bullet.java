package deck;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;

import java.util.*;

/**
 * TODO Bullet is used for graphics
 */
public class Bullet {

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
     * From ArrayList of Bullet to int array[num of R,num of Y, num of B]
     * @param bullets //TODO scrivedre meglio Java doc
     * @return
     */
    public int[] toIntArray(ArrayList<Bullet> bullets){
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

    public static String intArrayToString(int cubes[]){

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(cubes[0] + ",");
        stringBuilder.append(cubes[1] + ",");
        stringBuilder.append(cubes[2] + "]");

        return stringBuilder.toString();
    }

    public static String mapToString(Map<Color, Integer> map){

        int bullets[] = new  int[]{map.get(Color.RED), map.get(Color.YELLOW),map.get(Color.BLUE)};

        return intArrayToString(bullets);
    }
}