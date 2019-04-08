package board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private static Position p1;

    @BeforeAll
    public static void init(){

        p1 = new Position(1,2);

    }

    @Test
    void equals1() {

        //same position
        assertTrue(p1.equals(new Position(1,2)));
        //different position
        assertFalse(p1.equals(new Position(1,1)));
        //no postion to compare
        assertFalse(p1.equals(null));
        //no position
        assertFalse(p1.equals("Ciao"));


    }

    @Test
    void isNear() {

        assertTrue(p1.isNear(new Position(1,1)));
        assertTrue(p1.isNear(new Position(1,3)));
        assertTrue(p1.isNear(new Position(0,2)));
        assertTrue(p1.isNear(new Position(2,2)));

        assertFalse(p1.isNear(new Position(1,2)));
        assertFalse(p1.isNear(new Position(2,3)));
        assertFalse(p1.isNear(new Position(0,1)));

    }

    @Test
    void distance() {

        assertEquals(p1.distance(new Position(1,2)),0);
        assertEquals(p1.distance(new Position(1,3)),1);
        assertEquals(p1.distance(new Position(1,4)),2);

        assertEquals(p1.distance(new Position(2,2)),1);
        assertEquals(p1.distance(new Position(3,2)),2);


        assertEquals(p1.distance(new Position(2,3)),2);
        assertEquals(p1.distance(new Position(3,4)),4);

    }

    @Test
    void getX() {

        assertEquals(p1.getX(), 1);

    }

    @Test
    void getY() {
        assertEquals(p1.getY(), 2);

    }
}