package controller;

import board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//TODO fare i test

class BoardControllerTest {
    private  ArrayList<Player> players = new ArrayList<>();
    private  Board board = new Board(0);
    private  BoardController controller;
    private  Player p1;
    private  Player p2;
    private  Player p3;
    private  Player p4;

    @BeforeEach
    public void init(){
        p1 = new Player("Marco", board);
        p2 = new Player("Christian", board);
        p3 = new Player("Giovanni", board);
        p4 = new Player("Paolo", board);
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        controller = new BoardController(players, board);
    }

    @Test
    void testTurn(){
        assertEquals(p1, controller.getPlayer());
        controller.changeTurn();
        assertEquals(1, controller.getNumTurns());
        assertEquals(p2, controller.getPlayer());
        controller.changeTurn();
        controller.changeTurn();
        controller.changeTurn();
        assertEquals(0, controller.getNumTurns());
    }

    @Test
    void finalFrenzy(){
        controller.changeTurn();
        controller.changeTurn(); //turn of player 3
        assertTrue(controller.isFinalFrenzy());
        controller.setFinalFrenzyTurns();
        assertTrue(controller.verifyTwoTurnsFrenzy());
        controller.changeTurn(); //player 4
        assertTrue(controller.verifyTwoTurnsFrenzy());
        controller.changeTurn(); //player 1
        assertFalse(controller.verifyTwoTurnsFrenzy());
    }

    @Test
    void finalFrenzyFirstPlayer(){
        assertTrue(controller.isFinalFrenzy());
        controller.setFinalFrenzyTurns();
        assertFalse(controller.verifyTwoTurnsFrenzy());
        controller.changeTurn();
        assertFalse(controller.verifyTwoTurnsFrenzy());
        controller.changeTurn();
        assertFalse(controller.verifyTwoTurnsFrenzy());
    }

}