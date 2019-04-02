package board;

import java.util.*;


public class Door {

    private Cell cell1;
    private Cell cell2;

    public Door(Cell c1, Cell c2) {
        this.cell1 = c1;
        this.cell2 = c2;
    }

    public Cell getCell1() {
        return cell1;
    }

    public Cell getCell2() {
        return cell2;
    }
}