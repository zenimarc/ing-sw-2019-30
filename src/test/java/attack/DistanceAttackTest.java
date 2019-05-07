package attack;

import board.*;
import constants.EnumString;
import deck.Color;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DistanceAttackTest {
    private static Player p1,p2,p3;
    private static Board board;
    private static Billboard billboard;
    private static DistanceAttack distanceAttack;
    private static Cell cell1,cell2,cell3;
    private static int damage = 2;

    @BeforeAll
    public static void init(){
        board = new Board();

        distanceAttack = new DistanceAttack(EnumString.SUPPORT_ATTACK, damage,1,1,2,3);

        p1 = new Player("first",board);
        p2 = new Player("second",board);
        p3 = new Player("third",board);

        cell1 = new NormalCell();

        p1.setCell(cell1);
        p2.setCell(cell1);
        p3.setCell(cell1);
    }

    @Test
    void attack() {
        assertFalse(distanceAttack.attack(p1,p1, cell1));
    }

    @Test
    void attack1() {
        assertTrue(distanceAttack.attack(p1,new ArrayList<>(Arrays.asList(p2,p3))));

        assertEquals(p1.getNumDamages(), 0);
        assertEquals(p2.getNumDamages(), damage);
        assertEquals(p3.getNumDamages(), damage);

    }

    @Test
    void getDescription() {
    }
}