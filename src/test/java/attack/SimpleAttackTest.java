package attack;

import board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;

class SimpleAttackTest {

    private static Player p1, p2;
    private static Board board;
    private static SimpleAttack simpleAttack;
    private final static int testDamage = 3, testMark = 1;

    @BeforeAll
    static void init(){
        board = new Board();
        p1 = new Player("p1", board);
        p2 = new Player("p2", board);

        simpleAttack = new SimpleAttack("Atteck test", "Lorem ipsum",testDamage,testMark,1);
    }

    @Test
    void attack() {
        simpleAttack.attack(p1,p2);

        assertEquals(p1.getNumDamages(), 0);
        assertEquals(p2.getNumDamages(), testDamage);

        assertEquals(p1.getMarks(p2),0);
        assertEquals(p2.getMarks(p1),testMark);
    }

    @Test
    void getDescription() {
        assertEquals(simpleAttack.getDescription(),
                "Dai "+testDamage+" danni e "+testMark+" marchio/i a 1 bersaglio che puoi vedere.");
    }
}