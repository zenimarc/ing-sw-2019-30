package board;

import deck.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Billboards' instance is a billboard's astraction, know cell position
 */

public class Billboard {

    private HashMap<Cell, Position> billboard;
    private List<Door> doors;

    /**
     * Constructors
     */

    public Billboard(){
        doors = new ArrayList<>();
        billboard = new HashMap<>();
    }

    public Billboard(HashMap<Cell, Position> billboard, List<Door> doors){
        this.billboard = billboard;
        this.doors = doors;
    }

    /**
     * End constructors
     */

    /**
     * Say if exist in this a Door between c1 and c2 (or c2 and c1)
     * @param c1 First Cell
     * @param c2 Second Cell
     * @return true if exist door(c1, c2) else false
     */

    public boolean existPort(Cell c1, Cell c2) {
        return doors.stream()
                .anyMatch(x -> (x.getCell1() == c1 && x.getCell2() == c2) || (x.getCell1() == c2 && x.getCell2() == c1));
    }

    /**
     * This function verify if player can do a 1 step movement
     * start && goal same Position => false
     * start && goal distance == 1 && same color => true
     * start && goal distance == 1 && there is a door => true
     *
     * @param start start Cell
     * @param goal goal Cell
     * @return true if player can move from start to go, else false
     */
    private boolean canMoveSingleStep(Cell start, Cell goal){

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
     * This function return a list of same color's cells
     * @param color the color of cells
     * @return a list of Cell
     */
    private ArrayList<Cell> sameColorCell(Color color){
        return billboard.keySet().stream().filter(x -> x.color == color).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This function return a list of same color's cell whit a door
     * @param color the color of cells
     * @return a list of cell
     */
    private ArrayList<Cell> sameColorDoor(Color color){
        ArrayList<Cell> sameColor = sameColorCell(color);
        return sameColor.stream().filter(x ->
                (doors.stream()
                        .anyMatch(y-> y.getCell2()==x || y.getCell1()==x))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This function return the distance between two billboard's cells
     * @param c1
     * @param c2
     * @return distance c1->c2
     */
    private int cellDistance(Cell c1, Cell c2){
        return billboard.get(c1).distance(billboard.get(c2));
    }

    /**
     * This return a sub-List, from cells, attainable from c in step steps
     * @param cells list of cells
     * @param c start cell
     * @param step max num of steps
     * @return sub-list of cells
     */
    private ArrayList<Cell> attainableCell (ArrayList<Cell> cells, Cell c, int step){

        return cells.stream()
                .filter(x-> cellDistance(x,c)<=step)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * This function verify if a player can move from a cell "start" to a cell "goal" in a number of steps <= "steps"
     *
     * @param start start's cell
     * @param goal goal's cell
     * @param step max steps number
     * @return true if can move else false, if start cell == goal cell return false
     */
    public boolean canMove(Cell start, Cell goal, int step){

        if(step==1) return canMoveSingleStep(start,goal);

        if(start.color == goal.color){
            //Start and Goal same color so distance = num of cell
            return step>= cellDistance(start, goal);
        }else{
            //Start and Goal not same color

            //Select only doorCell attainable in step-1 steps from goalCell, if not exist return false
            ArrayList<Cell> goalCellDoor = attainableCell(sameColorDoor(goal.color), goal, step-1);
            if(goalCellDoor.isEmpty()) return false;

            //select only doorCell attainable in step-1 steps from startCell, if not exist return false
            ArrayList<Cell> startCellDoor = attainableCell(sameColorDoor(start.color), start, step-1);
            if(startCellDoor.isEmpty()) return false;

            //select door player can pass to arrive in goalCell
            //all possibleDoor has cell1.color == start.color
            ArrayList<Door> possibleDoor = doors.stream()
                    .filter(x->
                            (goalCellDoor.contains(x.getCell1()) && startCellDoor.contains(x.getCell2()))||
                                    (goalCellDoor.contains(x.getCell2()) && startCellDoor.contains(x.getCell1()))
                    )
                    .map(x -> {
                        if(x.getCell1().color == start.color) return x;
                        return new Door(x.getCell2(), x.getCell1());
                    })
                    .collect(Collectors.toCollection(ArrayList::new));

            for (Door door : possibleDoor){
                if(cellDistance(start,door.getCell1())+cellDistance(goal,door.getCell2())<=step) return true;
            }


        }


        return false;
    }

    public static void main(String[] args) {

        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        Cell c00 = new NormalCell(Color.GREEN);
        Cell c10 = new NormalCell(Color.BLUE);
        Cell c20 = new NormalCell(Color.BLUE);
//        Cell c30 = new NormalCell(Color.BLUE);

        Cell c01 = new NormalCell(Color.YELLOW);
        Cell c11 = new NormalCell(Color.YELLOW);
        Cell c21 = new NormalCell(Color.RED);
//        Cell c31 = new NormalCell(Color.RED);

//        Cell c02 = new NormalCell(Color.YELLOW);
//        Cell c12 = new NormalCell(Color.YELLOW);
//        Cell c22 = new NormalCell(Color.WHITE);
//        Cell c32 = new NormalCell();


        mappaProva.put(c00, new Position(0, 0));
        mappaProva.put(c10, new Position(1, 0));
        mappaProva.put(c20, new Position(2, 0));
//        mappaProva.put(c30, new Position(3, 0));

        mappaProva.put(c01, new Position(0, 1));
        mappaProva.put(c11, new Position(1, 1));
        mappaProva.put(c21, new Position(2, 1));
//        mappaProva.put(c31, new Position(3, 1));

//        mappaProva.put(c02, new Position(0, 2));
//        mappaProva.put(c12, new Position(1, 2));
//        mappaProva.put(c22, new Position(2, 2));
//        mappaProva.put(c32, new Position(3, 2));


        doors.add(new Door(c00, c01));
        doors.add(new Door(c00, c10));
        doors.add(new Door(c10, c11));
//        doors.add(new Door(c30, c31));
//        doors.add(new Door(c21, c22));
//        doors.add(new Door(c12, c22));

        Billboard myBillboard = new Billboard(mappaProva,doors);

        System.out.println("Posso andare da 00 a 11 in 1 passo: "+myBillboard.canMove(c00,c11,1));
        System.out.println("Posso andare da 00 a 11 in 2 passo: "+myBillboard.canMove(c00,c11,2));
        System.out.println("Posso andare da 00 a 11 in 3 passo: "+myBillboard.canMove(c00,c11,3));

    }

}
