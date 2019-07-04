package board;

import board.billboard.Billboard;
import constants.Color;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BillboardTest {

    private static Billboard myBillboard;
    private static HashMap<Cell, Position> mappaProva = new HashMap<>();
    private static ArrayList<Door> doors = new ArrayList<>();

    private static Cell c00 = new NormalCell(Color.GREEN);
    private static Cell c10 = new RegenerationCell(Color.BLUE);
    private static Cell c20 = new NormalCell(Color.BLUE);
    private static Cell c30 = new NormalCell(Color.BLUE);

    private static Cell c01 = new NormalCell(Color.YELLOW);
    private static Cell c11 = new NormalCell(Color.YELLOW);
    private static Cell c21 = new NormalCell(Color.RED);
    private static Cell c31 = new RegenerationCell(Color.RED);

    private static Cell c02 = new RegenerationCell(Color.YELLOW);
    private static Cell c12 = new NormalCell(Color.YELLOW);
    private static Cell c22 = new NormalCell(Color.WHITE);
    private static Cell c32 = new NormalCell();

    @BeforeAll
    public static void init(){

        //CREATE A MAP

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

        myBillboard = new Billboard(mappaProva,doors);
    }



    @Test
    void hasDoor() {
        for(Door door:doors){
            assertEquals(myBillboard.hasDoor(door.getCell1(),door.getCell2()),true);
        }
        assertEquals(myBillboard.hasDoor(c00,c22), false);
        assertEquals(myBillboard.hasDoor(c00,c11), false);
    }

    @Test
    void canMove() {
        //test non-move
        assertTrue(myBillboard.canMove(c22,c22,4));

        //test canMoveSingleStep
        assertTrue(myBillboard.canMove(c01,c11,1));
        assertFalse(myBillboard.canMove(c11,c31,1));
        assertFalse(myBillboard.canMove(c11,c21,1));
        for(Door door:doors){
            assertTrue(myBillboard.canMove(door.getCell1(),door.getCell2(),1));
        }

        //test canMove, startCell and goalCell same color
        assertFalse(myBillboard.canMove(c01,c12,1));
        assertTrue(myBillboard.canMove(c01,c12,2));
        assertTrue(myBillboard.canMove(c01,c12,3));

        //test canMove near room
        assertTrue(myBillboard.canMove(c00,c12,3));
        assertFalse(myBillboard.canMove(c00,c12,2));

        assertTrue(myBillboard.canMove(c30,c12,4));
        assertFalse(myBillboard.canMove(c30,c12,3));

        assertTrue(myBillboard.canMove(c30,c11,3));
        assertFalse(myBillboard.canMove(c30,c11,2));

        // Find way from start room to goal room passing in other room
        assertTrue(myBillboard.canMove(c00,c31, 4));
        assertFalse(myBillboard.canMove(c00,c31, 3));


    }

    @Test
    void hasSameColor(){
        assertTrue(myBillboard.hasSameColor(c10, c20));
        assertFalse(myBillboard.hasSameColor(c10, c00));
    }

    @Test
    void isVisible(){
        assertTrue(myBillboard.isVisible(c00, c12));
        assertFalse(myBillboard.isVisible(c12, c00));
    }
    @Test
    void visibleCellsTest(){
        List<Cell> cells =  new ArrayList<>();
        cells.add(c00);
        cells.add(c10);
        cells.add(c20);
        cells.add(c30);
        cells.add(c01);
        cells.add(c02);
        cells.add(c11);
        cells.add(c12);
        assertTrue(myBillboard.visibleCells(c00).containsAll(cells));

    }
    @Test
    void getCellFromPositionTest(){
        HashMap<Cell, Position> cellMap = new HashMap<>();
        cellMap.put(c01, new Position(0,1));
        cellMap.put(c02, new Position(0,2));
        Billboard billboard1 = new Billboard(cellMap, doors);
        assertEquals(billboard1.getCellFromPosition(new Position(0,1)), c01);
        assertEquals(billboard1.getCellFromPosition(new Position(0,2)), c02);
        assertNull(billboard1.getCellFromPosition(new Position(0,3)));
    }

}