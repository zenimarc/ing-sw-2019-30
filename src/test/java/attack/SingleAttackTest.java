package attack;

import board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;

class SingleAttackTest {

    private static Player p1, p2;
    private static Board board;

    @BeforeAll
    static void init(){
        board = new Board();
        p1 = new Player("p1", board);
        p2 = new Player("p2", board);

    }

    @Test
    void attack() {

        SingleAttack singleAttack = new SingleAttack("Atteck test", "Lorem ipsum",2,1);
        singleAttack.attack(p1,p2);

        assertEquals(p1.getPlayerBoard().getNumDamages(), 0);
        assertEquals(p2.getPlayerBoard().getNumDamages(), 2);

        assertEquals(p1.getPlayerBoard().getMarks(p2),0);
        assertEquals(p2.getPlayerBoard().getMarks(p1),1);


    }
}