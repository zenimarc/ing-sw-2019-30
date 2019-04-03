package player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    @BeforeEach
    public void before() {
        p1 = new Player("Marco");
        p2 = new Player("Christian");
        p3 = new Player("Giovanni");
        p4 = new Player("Paolo");
    }

    @Test
    void addDamageTest() {
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(1, p1.getPlayerBoard().getNumDamages());
        p1.getPlayerBoard().addDamage(p3);
        assertEquals(2, p1.getPlayerBoard().getNumDamages());
        for(int i=0; i<12; i++)
                p1.getPlayerBoard().addDamage(p2);
        assertEquals(12, p1.getPlayerBoard().getNumDamages());
    }

    @Test
    void getPointsTest() {
        p1.getPlayerBoard().addDamage(p4);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p2);
        p1.getPlayerBoard().addDamage(p3);
        p1.getPlayerBoard().addDamage(p4);
        assertEquals(9,p1.getPlayerBoard().getPoints().get(p4));
    }
}