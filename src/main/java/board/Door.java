package board;

import java.util.*;

/**
 * Door is a class used to know if two adjacent cells are connected
 */

public class Door {

    private Cell cell1;
    private Cell cell2;

    /**
     * Constructors
     */

    public Door(Cell c1, Cell c2) {
        this.cell1 = c1;
        this.cell2 = c2;
    }

    /**
     * End Constructors
     */

    /**
     * This function returns the first cell of two cells connected by a door
     * @return the first cell
     */
    public Cell getCell1() {
        return cell1;
    }

    /**
     * This function returns the second cell of two cells connected by a door
     * @return the second cell
     */
    public Cell getCell2() {
        return cell2;
    }
}