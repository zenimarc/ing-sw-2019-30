package player;

import board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {
    private Board board;
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    @BeforeEach
    public void before() {
        board = new Board(1);
        p1 = new Player("Marco", board);
        p2 = new Player("Christian", board);
        p3 = new Player("Giovanni", board);
        p4 = new Player("Paolo", board);
    }

    @Test
    void playerBoardTest() {
        assertEquals(0, p1.getPlayerBoard().getNumDeaths());
    }


    @Test
    void addDamageTest() {
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(1, p1.getPlayerBoard().getNumDamages());
        p1.getPlayerBoard().addDamage(p3);
        assertEquals(2, p1.getPlayerBoard().getNumDamages());
        for (int i = 0; i < 12; i++)
            p1.getPlayerBoard().addDamage(p2);
        assertEquals(12, p1.getPlayerBoard().getNumDamages());

        p3.getPlayerBoard().addMark(p2);
        p3.getPlayerBoard().addMark(p2);
        assertEquals(2, p3.getPlayerBoard().getMarks(p2));
        p3.getPlayerBoard().addDamage(p2);
        assertEquals(3, p3.getPlayerBoard().getNumDamages());
        assertEquals(0, p3.getPlayerBoard().getMarks(p2));
        for (int i = 0; i < 12; i++)
            p3.getPlayerBoard().addDamage(p2);
        assertEquals(12, p3.getPlayerBoard().getNumDamages());
        for (int i = 0; i < 4; i++)
            p3.getPlayerBoard().addMark(p2);
        assertEquals(3, p3.getPlayerBoard().getMarks(p2));
        p3.getPlayerBoard().addDamage(p2);
        assertEquals(12, p3.getPlayerBoard().getNumDamages());
        assertEquals(0, p3.getPlayerBoard().getMarks(p2));

        p2.getPlayerBoard().clearDamages();
        p2.getPlayerBoard().addDamage(p1, 5);
        assertEquals(5, p2.getPlayerBoard().getNumDamages());
        p2.getPlayerBoard().addDamage(p1, 10);
        assertEquals(12, p2.getPlayerBoard().getNumDamages());


    }

    @Test
    void getPointsTest() {
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(9, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(6, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(4, p1.getPlayerBoard().getPoints().get(p2));
    }

    @Test
    void getPointsWithDeathTest() {
        p1.getPlayerBoard().addSkull();
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(7, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(4, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(2, p1.getPlayerBoard().getPoints().get(p2));
        p1.getPlayerBoard().addSkull();
        p1.getPlayerBoard().addSkull();
        p1.getPlayerBoard().addSkull();
        assertEquals(2, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p2));
        p1.getPlayerBoard().addSkull();
        p1.getPlayerBoard().addSkull();
        assertEquals(2, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p2));

    }

    @Test
    void getPointsInFrenzyMode() {
        board.decrementSkull();
        assertTrue(board.isFinalFrenzy());
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(2, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p2));
        p1.getPlayerBoard().addSkull();
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p4));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p3));
        assertEquals(1, p1.getPlayerBoard().getPoints().get(p2));


    }

    @Test
    void addMarkTest() {
        for (int i = 0; i < 5; i++)
            p1.getPlayerBoard().addMark(p2);
        p1.getPlayerBoard().addMark(p3);
        assertEquals(3, p1.getPlayerBoard().getMarks(p2));
        assertEquals(1, p1.getPlayerBoard().getMarks(p3));
        p1.getPlayerBoard().addMark(p3, 2);
        assertEquals(3, p1.getPlayerBoard().getMarks(p3));

    }

    @Test
    void numDeathTest() {
        p1.getPlayerBoard().increaseNumDeath();
        assertEquals(1, p1.getPlayerBoard().getNumDeaths());
    }

    @Test
    void clearDamageTest(){
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        assertEquals(3, p1.getPlayerBoard().getNumDamages());
        p1.getPlayerBoard().clearDamages();
        assertEquals(0, p1.getPlayerBoard().getNumDamages());
    }

}