package view;

import board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardViewTest {
    Board gameBoard;
    Player p1;
    PlayerBoardView p1view;

    @BeforeEach
    void setUp() {
        gameBoard = new Board(1);
        p1 = new Player("Marco", gameBoard);
        p1.addDamage(new Player("Alberto"), 8);
        p1.addDamage(new Player("L"), 6);
        p1view = new PlayerBoardView(p1);
    }

    @Test
    void drawDamageTrack() {
        p1view.drawDamageTrack();
    }
}