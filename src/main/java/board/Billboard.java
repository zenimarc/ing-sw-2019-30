package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.CompositeName;

public class Billboard {


    private HashMap<Cell, Position> billboard;
    private List<Door> doors;


    public Billboard(){
        doors = new ArrayList<Door>();
    }


    public boolean canMove(Cell start, Cell goal){

        Position startPosition = billboard.get(start);
        Position goalPosition = billboard.get(goal);

        if(startPosition.equals(goalPosition)){return false;}

        return false;
    }



}
