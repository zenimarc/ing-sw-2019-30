package board;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * This class is used to determine the position of an object
 */

public class Position {
    private int x;
    private int y;

    /**
     * Constructors
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * hashcode() function
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * This function verifies if two objects have the same position
      * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj== null) return false;

        if(obj.getClass()==Position.class){
            Position p = (Position) obj;

            return(this.x == p.x && this.y == p.y);
        }
        return false;
    }

    /**
     * Position p is near this if the distance is a vertical step or a horizontal step.
     * @param p other position
     * @return true if if the distance is a vertical step or a horizontal step else @return false
     */
    public boolean isNear(Position p){
        return (x == p.x && (y==p.y-1 || y==p.y+1)) || (y == p.y && (x==p.x-1 || x==p.x+1));
    }

    //NB not see wall
    public int distance(Position p){
        return abs(this.x-p.x) + abs(this.y-p.y);
    }

    /**
     * This function returs the x coordinate of an object
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     * This function returs the x coordinate of an object
     * @return y position
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+this.x+","+this.y+")";
    }
}
