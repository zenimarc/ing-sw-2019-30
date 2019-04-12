package deck;

import java.util.*;

/**
 * TODO Bullet is used for graphics
 */
public class Bullet {

    private Color color;

    /**
     * Constructors
     */

    public Bullet(Color color) {
        this.color = color;
    }

    /**
     * End constructors
     */

    /**
     * From ArrayList of Bullet to int array[num of R,num of Y, num of B]
     * @param bullets
     * @return
     */
    public int[] toIntArray(ArrayList<Bullet> bullets){

        int[] colorArray = new int[3];
        int i;

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

}