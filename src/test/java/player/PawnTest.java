package player;

import board.Board;
import board.Cell;
import board.RegenerationCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private Pawn pawn;
    private Cell cell1;
    private Player player1;
    @BeforeEach
    void setUp(){
        player1 = new Player("marco");
        pawn = new Pawn(player1);
        cell1 = new RegenerationCell();
    }

    @Test
    void setCellTest() {
        pawn.setCell(cell1);
        assertEquals(cell1, pawn.getCell());
    }
    @Test
    void getPlayerTest() {
        assertEquals(player1, pawn.getPlayer());
    }
}