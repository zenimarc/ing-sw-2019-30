package attack;

import board.Board;
import board.Cell;
import board.NormalCell;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MoveAttackTest {

    private static Player p1,p2,p3;
    private static Board board;
    private static MoveAttack moveAttack;
    private static Cell cell1 = new NormalCell();
    private static Cell cell2 = new NormalCell();
    private static int damage = 1;


    @BeforeAll
    public static void init(){
        board = new Board();

        moveAttack = new MoveAttack(EnumTargetSet.VISIBLE, EnumAttackName.BASE_ATTACK_NAME, "description", 2,damage,1);

        p1 = new Player("first",board);
        p2 = new Player("second",board);
        p3 = new Player("third",board);
    }

    @Test
    void attack() {
        assertFalse(moveAttack.attack(p1,new ArrayList<>(Arrays.asList(p2,p3))));
    }

    @Test
    void attack1() {
        p1.setPawnCell(cell1);
        p2.setPawnCell(cell1);
        p3.setPawnCell(cell1);

        assertTrue(moveAttack.attack(p1,new ArrayList<>(Arrays.asList(p2,p3)), cell2));
        assertTrue(p1.getCell()==cell1);
        assertTrue(p2.getCell()==cell2);
        assertTrue(p3.getCell()==cell2);

        assertEquals(p1.getNumDamages(), 0);
        assertEquals(p2.getNumDamages(), damage);
        assertEquals(p3.getNumDamages(), damage);

    }

}