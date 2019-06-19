package attack;

import board.Board;
import constants.EnumAttackName;
import controller.EnumTargetSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;

class SimpleAttackTest {

    private static Player p1, p2;
    private static SimpleAttack simpleAttack;
    private final static int testDamage = 3, testMark = 1;

    @BeforeAll
    static void init(){
        p1 = new Player("p1");
        p2 = new Player("p2");

        simpleAttack = new SimpleAttack(EnumTargetSet.VISIBLE, EnumAttackName.TEST_ATTACK, "Lorem ipsum",testDamage,testMark,1);
    }

    @Test
    void attack() {
        simpleAttack.attack(p1,p2);

        assertEquals(p1.getNumDamages(), 0);
        assertEquals(p2.getNumDamages(), testDamage);

        assertEquals(p1.getMarks(p2),0);
        assertEquals(p2.getMarks(p1),testMark);
    }

}