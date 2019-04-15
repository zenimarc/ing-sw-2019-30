package weapon;

import attack.SimpleAttack;
import board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DestructorTest {

    private static Player p1, p2,p3;
    private static Board board;
    private static Destructor destructor;

    @BeforeAll
    static void init() {
        board = new Board();
        p1 = new Player("p1", board);
        p2 = new Player("p2", board);
        p3 = new Player("p3", board);
        destructor = new Destructor();
    }

    @Test
    void shoot() {
        //destructor not loaded
        assertFalse(destructor.shoot(0,p1,p2));

        //typeAttack: out of range
        destructor.isLoaded = true;
        assertFalse(destructor.shoot(3,p1,p2));

        //shoot type 0
        assertTrue(destructor.shoot(0,p1,p2));
        assertEquals(((SimpleAttack) destructor.getAttack(0)).getDamage(), p2.getNumDamages());
        assertEquals(((SimpleAttack) destructor.getAttack(0)).getMark(), p2.getMarks(p1));

        //shoot type 1

        p1.addAmmo(new int[]{3,3,3});
        p2 = new Player("p2", board);

        assertTrue(destructor.shoot(1,p1, new ArrayList<Player>(Arrays.asList(p2,p3))));

        assertEquals(((SimpleAttack) destructor.getAttack(0)).getDamage(), p2.getNumDamages());
        assertEquals(((SimpleAttack) destructor.getAttack(1)).getDamage(), p3.getNumDamages());


        assertEquals(((SimpleAttack) destructor.getAttack(0)).getMark(), p2.getMarks(p1));
        assertEquals(((SimpleAttack) destructor.getAttack(1)).getMark(), p3.getMarks(p1));


    }
}