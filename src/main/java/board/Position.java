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

        if(obj.getClass()==Position.class){
            Position p = (Position) obj;

            if(this.x == p.x && this.y == p.y){
                return  true;
            }
        }
        return false;
    }

    public boolean isNear(Position p){
        if(x == p.x && (y==p.y-1 || y==p.y+1)){return true;}
        if(y == p.y && (x==p.x-1 || x==p.x+1)){return true;}
        return false;
    }

    //NB not see wall
    public int distance(Position p){
        return abs(this.x-p.x) + abs(this.y-p.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
