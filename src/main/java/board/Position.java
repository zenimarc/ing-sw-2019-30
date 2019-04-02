package board;

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
}
