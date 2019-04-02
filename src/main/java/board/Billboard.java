package board;

import deck.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.CompositeName;

public class Billboard {


    private HashMap<Cell, Position> billboard;
    private List<Door> doors;


    /**
     * Constructors
     */

    public Billboard(){
        doors = new ArrayList<Door>();
        billboard = new HashMap<>();
    }

    public Billboard(HashMap<Cell, Position> billboard, List<Door> doors){
        this.billboard = billboard;
        this.doors = doors;
    }

    /**
     * End constructors
     */

    public boolean existPort(Cell c1, Cell c2) {

        return doors.stream()
                .filter(x -> (x.getCell1() == c1 && x.getCell2() == c2) || (x.getCell1() == c2 && x.getCell2() == c1)).findFirst().isPresent();

    }

    /**
     * start && goal same Position => false
     * start && goal distance == 1 && same color => true
     * start && goal distance == 1 && there is a door => true
     */
    private boolean canMove(Cell start, Cell goal){

        Position startPosition = billboard.get(start);
        Position goalPosition = billboard.get(goal);

        if(startPosition.equals(goalPosition))return false;

        if(startPosition.isNear(goalPosition)) {
            if (start.color == goal.color) return true;
            if (existPort(start, goal)) return true;
        }

        return false;
    }


    /**
     * return true if my step>= necessary step
     */
    public boolean canMove(Cell start, Cell goal, int step){

        if(step==1) return canMove(start,goal);

        Position startPosition = billboard.get(start);
        Position goalPosition = billboard.get(goal);

        //Se stesso colore basta contare numero di passi
        if(start.color == goal.color){
            return step>= goalPosition.distance(startPosition);
        }else{
            //TODO colore diverso -gio
            //TODO rivedere stream() - gio

            //Find port from actual color
            List<Cell> portMyColor = billboard.keySet().stream().filter(x -> {
                if(doors.stream().filter(y -> y.getCell1()==x || y.getCell2()==x).findFirst().isPresent()) return true;
                else return false;
            }).collect(Collectors.toList());


        }


        return false;
    }

    public static void main(String args[]) {

        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.GREEN);
        Cell c10 = new NormalCell(Color.BLUE);
        Cell c20 = new NormalCell(Color.BLUE);
        Cell c30 = new NormalCell(Color.BLUE);

        Cell c01 = new NormalCell(Color.YELLOW);
        Cell c11 = new NormalCell(Color.YELLOW);
        Cell c21 = new NormalCell(Color.RED);
        Cell c31 = new NormalCell(Color.RED);

        Cell c02 = new NormalCell(Color.YELLOW);
        Cell c12 = new NormalCell(Color.YELLOW);
        Cell c22 = new NormalCell(Color.WHITE);
        Cell c32 = new NormalCell();


        mappaProva.put(c00, new Position(0, 0));
        mappaProva.put(c10, new Position(1, 0));
        mappaProva.put(c20, new Position(2, 0));
        mappaProva.put(c30, new Position(3, 0));

        mappaProva.put(c01, new Position(0, 1));
        mappaProva.put(c11, new Position(1, 1));
        mappaProva.put(c21, new Position(2, 1));
        mappaProva.put(c31, new Position(3, 1));

        mappaProva.put(c02, new Position(0, 2));
        mappaProva.put(c12, new Position(1, 2));
        mappaProva.put(c22, new Position(2, 2));
        mappaProva.put(c32, new Position(3, 2));


        doors.add(new Door(c00, c01));
        doors.add(new Door(c00, c10));
        doors.add(new Door(c10, c11));
        doors.add(new Door(c30, c31));
        doors.add(new Door(c21, c22));
        doors.add(new Door(c12, c22));

        Billboard myBillboard = new Billboard(mappaProva,doors);

        System.out.println(myBillboard.canMove(c01,c12));

        System.out.println(myBillboard.canMove(c01,c12,3));


    }



}
