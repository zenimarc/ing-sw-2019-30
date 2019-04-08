package board;

import static java.lang.Math.abs;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj== null) return false;

        if(obj.getClass()==Position.class){
            Position p = (Position) obj;

            if(this.x == p.x && this.y == p.y){
                return  true;
            }
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
     *
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y position
     */
    public int getY() {
        return y;
    }
}
