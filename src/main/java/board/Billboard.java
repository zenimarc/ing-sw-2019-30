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

    public boolean existPort(Cell c1, Cell c2) {

        return doors.stream()
                .filter(x -> (x.getCell1() == c1 && x.getCell2() == c2) || (x.getCell1() == c2 && x.getCell2() == c1)).findFirst().isPresent();

    }

    /**
     * start && goal same Position => false
     * start && goal distance == 1 && same color => true
     * start && goal distance == 1 && there is a door => true
     */
    public boolean canMove(Cell start, Cell goal){

        Position startPosition = billboard.get(start);
        Position goalPosition = billboard.get(goal);

        if(startPosition.equals(goalPosition))return false;

        if(startPosition.isNear(goalPosition)) {
            if (start.color == goal.color) return true;
            if (existPort(start, goal)) return true;
        }

        return false;
    }

    public boolean canMove(Cell start, Cell goal, int step){



        return false;
    }



}
