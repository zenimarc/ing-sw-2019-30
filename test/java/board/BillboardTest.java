package board;

import deck.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BillboardTest {

    Billboard myBillboard;
    HashMap<Cell, Position> mappaProva = new HashMap<>();
    ArrayList<Door> doors = new ArrayList<>();

    @BeforeAll
    public void init(){

        //CREATE A MAP

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

        myBillboard = new Billboard(mappaProva,doors);
    }



    @Test
    void existPort() {




    }

    @Test
    void canMove() {
    }
}