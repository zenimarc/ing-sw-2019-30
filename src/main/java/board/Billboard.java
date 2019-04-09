package board;

import deck.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Billboards' instance is a billboardCell's astraction, know cell position
 */

public class Billboard {

    private HashMap<Cell, Position> billboardCell;
    private List<Door> doors;

    /**
     * Constructors
     */

    public Billboard(){
        doors = new ArrayList<>();
        billboardCell = new HashMap<>();
    }

    public Billboard(HashMap<Cell, Position> billboardCell, List<Door> doors){
        this.billboardCell = billboardCell;
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

    public boolean hasDoor(Cell c1, Cell c2) {
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

        Position startPosition = billboardCell.get(start);
        Position goalPosition = billboardCell.get(goal);

        if(startPosition.equals(goalPosition))return false;

        if(startPosition.isNear(goalPosition)) {
            if (start.color == goal.color) return true;
            if (hasDoor(start, goal)) return true;
        }

        return false;
    }

    /**
     * This function return a list of same color's cells
     * @param color the color of cells
     * @return a list of Cell
     */
    private ArrayList<Cell> sameColorCell(Color color){
        return billboardCell.keySet().stream().filter(x -> x.color == color).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This function return a list of same color's cell with a door
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
     * This function return the distance between two billboardCell's cells
     * @param c1
     * @param c2
     * @return distance c1->c2
     */
    private int cellDistance(Cell c1, Cell c2){
        return billboardCell.get(c1).distance(billboardCell.get(c2));
    }

    /**
     * This return a sub-List, from cells, attainable from c in step number of steps
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
     *
     * @param startAttainableDoor
     * @param goalAttainableDoor
     * @return
     */

    private ArrayList<Door> singleDoorsPlayerCanPass( ArrayList<Cell> startAttainableDoor, ArrayList<Cell> goalAttainableDoor){
        return doors.stream()
                .filter(x->
                        (goalAttainableDoor.contains(x.getCell1()) && startAttainableDoor.contains(x.getCell2()))||
                                (goalAttainableDoor.contains(x.getCell2()) && startAttainableDoor.contains(x.getCell1()))
                )//attainable
                .map(x -> {
                    if(x.getCell1().color == startAttainableDoor.get(0).color) return x;
                    return new Door(x.getCell2(), x.getCell1());
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * @param startAttainableDoorCell
     * @param goalAttainableDoorCell
     * @return
     */
    private HashMap<Door,Door> doubleDoorsPlayerCanPass(ArrayList<Cell> startAttainableDoorCell, ArrayList<Cell> goalAttainableDoorCell){
        HashMap<Door,Door> possibleDoors = new HashMap<>();
        ArrayList<Door> startDoor = new ArrayList<>();
        ArrayList<Door> goalDoor = new ArrayList<>();

        for(Door door:this.doors){
            if (startAttainableDoorCell.contains(door.getCell1())) startDoor.add(door);
            if (startAttainableDoorCell.contains(door.getCell2())) startDoor.add(new Door(door.getCell2(),door.getCell1()));

            if (goalAttainableDoorCell.contains(door.getCell2())) goalDoor.add(door);
            if (goalAttainableDoorCell.contains(door.getCell1())) goalDoor.add(new Door(door.getCell2(),door.getCell1()));
        }

        for(Door door: startDoor){
            for(Door door1 : goalDoor){
                if (door.getCell2().getColor()==door1.getCell1().getColor()){
                    possibleDoors.put(door,door1);
                }
            }
        }

        return possibleDoors;

    }

    /**
     * Find way from start room to goal room without passing in other room
     * @param sc start cell
     * @param gc goal cell
     * @param possibleDoors linking door from start cell to goal cell
     * @param step max step number that player can do
     * @return true if there is one (or more) way to go from startCell to goalCell
     */
    private boolean thereIsWalkableSimpleWay(Cell sc, Cell gc, ArrayList<Door> possibleDoors, int step){
        for (Door door : possibleDoors) {
            //+1 is for room's switch
            if (cellDistance(sc, door.getCell1()) + cellDistance(gc, door.getCell2())+1 <= step) return true;
        }
        return false;
    }

    /**
     * Find way from start room to goal room passing in other room
     * @param sc
     * @param gc
     * @param possibleDoors
     * @param step
     * @return
     */
    private boolean thereIsWalkableComplexWay(Cell sc, Cell gc, HashMap<Door,Door> possibleDoors, int step){

        for(Map.Entry<Door,Door> entry: possibleDoors.entrySet()){
            if(cellDistance(sc,entry.getKey().getCell1())
                    +cellDistance(entry.getKey().getCell2(), entry.getValue().getCell1())
                    +cellDistance(entry.getValue().getCell2(), gc)
                    +2 <=step) return true;
        }
        return false;
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
            //Near room

            //Select only doorCell attainable in step-1 steps from goalCell, if not exist return false
            ArrayList<Cell> goalCellsDoor = attainableCell(sameColorDoor(goal.color), goal, step-1);
            if(goalCellsDoor.isEmpty()) return false;

            //select only doorCell attainable in step-1 steps from startCell, if not exist return false
            ArrayList<Cell> startCellsDoor = attainableCell(sameColorDoor(start.color), start, step-1);
            if(startCellsDoor.isEmpty()) return false;

            //select door that player can pass to arrive in goalCell
            //all possibleDoors have cell1.color == start.color
            ArrayList<Door> possibleDoors = singleDoorsPlayerCanPass(startCellsDoor, goalCellsDoor);

            //Find if exist one (or more) way from start room to goal room without pass in other room
            if(thereIsWalkableSimpleWay(start,goal, possibleDoors, step)) return true;

            //Not near room (pass two doors)
            HashMap<Door,Door> doubleDoors = doubleDoorsPlayerCanPass(startCellsDoor,goalCellsDoor);
            if(thereIsWalkableComplexWay(start,goal,doubleDoors,step)) return true;




        }
        return false;
    }

    /**
     * This function verifies if two cells have the same color
     * @param cell1
     * @param cell2
     * @return true if they have the same color, else false
     */
    public boolean hasSameColor(Cell cell1, Cell cell2){
        return (cell1.getColor() == cell2.getColor());
    }

    /**
     * This function verifies if I can see a specific cell in a different room from another cell
     * @param cell1 the cell from which I want to see
     * @param cell2 the cell which I want to see
     * @return true if I can see the other cell, else false
     */
    public boolean canSeeThroughDoor(Cell cell1, Cell cell2){
        ArrayList<Cell> cellsWithDoor;
        cellsWithDoor = sameColorCell(cell2.getColor());
        for(Cell cell : cellsWithDoor)
            if(hasDoor(cell1, cell))
                return true;
        return false;
    }

    /**
     * This function verifies if a player can see another one
     * @param cell1 the cell from which I want to see
     * @param cell2 the cell which I want to see
     * @return true if they have the same color, else false
     */

    public boolean isVisible(Cell cell1, Cell cell2){
        return (hasSameColor(cell1, cell2) || canSeeThroughDoor(cell1, cell2));
    }


}
