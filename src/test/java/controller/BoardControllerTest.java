package controller;

import board.*;
import com.google.gson.Gson;
import deck.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static controller.EnumTargetSet.*;
import static org.junit.jupiter.api.Assertions.*;

//TODO fare i test

class BoardControllerTest {
    private  ArrayList<Player> players = new ArrayList<>();
    private  Board board;
    private static Billboard billboard;
    private  BoardController controller;
    private  Player p1;
    private  Player p2;
    private  Player p3;
    private  Player p4;

    private static Cell c00 = new NormalCell(Color.GREEN);
    private static Cell c10 = new RegenerationCell(Color.BLUE);
    private static Cell c20 = new NormalCell(Color.BLUE);
    private static Cell c30 = new NormalCell(Color.BLUE);

    private static Cell c01 = new NormalCell(Color.YELLOW);
    private static Cell c11 = new NormalCell(Color.YELLOW);
    private static Cell c21 = new NormalCell(Color.RED);
    private static Cell c31 = new RegenerationCell(Color.RED);

    private static Cell c02 = new RegenerationCell(Color.YELLOW);
    private static Cell c12 = new NormalCell(Color.YELLOW);
    private static Cell c22 = new NormalCell(Color.WHITE);
    private static Cell c32 = new NormalCell();

    @BeforeAll
    public static void setup(){
        billboard = genBillboard();
    }

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
        board = new Board(0, billboard);
        controller = new BoardController(players, board);
    }

    @Test
    void BoardControllerConstructorTest(){
        for (Player player : controller.getListOfPlayers()) {
            assertTrue(controller.getListOfPlayers().contains(player));
            assertNotNull(controller.getPlayerController(player));
        }
    }
    @Test
    void getPotentialTargetsTest(){
        p1.setCell(c01);
        p2.setCell(c12);
        p3.setCell(c22);
        p4.setCell(c22);
        ArrayList<Player> potentialTargets = new ArrayList<>();
        potentialTargets.add(p1);
        potentialTargets.add(p2);
        assertTrue(controller.getPotentialTargets(c00, VISIBLE).containsAll(potentialTargets));
        assertTrue(controller.getPotentialTargets(c02, SAME_ROOM).containsAll(potentialTargets));
        potentialTargets.remove(p2); //change potential targets to test same cell
        assertTrue(controller.getPotentialTargets(c01, SAME_CELL).containsAll(potentialTargets));
        potentialTargets.clear();
        //testing same cell with 2 player
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c22, SAME_CELL).containsAll(potentialTargets));
        //Testing cardinal bypass wall
        potentialTargets.clear();
        p3.setCell(c21);
        p4.setCell(c10);
        potentialTargets.add(p1);
        potentialTargets.add(p2);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c11, CARDINAL_WALL_BYPASS).containsAll(potentialTargets));
        //Testing Cardinal normal
        potentialTargets.clear();
        p1.setCell(c00);
        p2.setCell(c10);
        p3.setCell(c02);
        p4.setCell(c01); //same cell of the shooter case
        potentialTargets.add(p1);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c01, CARDINAL).containsAll(potentialTargets));
        p2.setCell(c22);
        assertTrue(controller.getPotentialTargets(c01, CARDINAL).containsAll(potentialTargets));
        //test2 cardinal with different positions
        potentialTargets.clear();
        p1.setCell(c20);
        p2.setCell(c11);
        p3.setCell(c22);
        p4.setCell(c31);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c21, CARDINAL).containsAll(potentialTargets));

    }

    @Test
    void getPotentialDestinationCellsTest(){
        p1.setCell(c00);
        ArrayList<Cell> potentialTargets = new ArrayList<>();
        potentialTargets.add(c01);
        potentialTargets.add(c02);
        potentialTargets.add(c11);
        potentialTargets.add(c20);
        potentialTargets.add(c10);
        potentialTargets.add(c00);
        assertTrue(potentialTargets.containsAll(controller.getPotentialDestinationCells(p1.getCell(), 2)));
        potentialTargets.add(c12);
        potentialTargets.add(c21);
        potentialTargets.add(c31);
        potentialTargets.add(c30);
        potentialTargets.add(c22);
        potentialTargets.add(c32);
        assertTrue(potentialTargets.containsAll(controller.getPotentialDestinationCells(p1.getCell(), 4)));
        assertFalse(controller.getBoard().getBillboard().canMove(p1.getCell(), c32, 5));

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
    private static Billboard genBillboard(){
        HashMap<Cell, Position> mappaProva = new HashMap<>();
        ArrayList<Door> doors = new ArrayList<>();

        mappaProva.put(c00, new Position(0, 0));
        mappaProva.put(c10, new Position(1, 0));
        mappaProva.put(c20, new Position(2, 0));
        mappaProva.put(c30, new Position(3, 0));

        mappaProva.put(c01, new Position(0, 1));
        mappaProva.put(c11, new Position(1, 1));
        mappaProva.put(c21, new Position(2, 1));
        mappaProva.put(c31, new Position(3, 1));

        mappaProva.put(c02, new Position(0, 2));
        mappaProva.put(c12, new Position(1, 2));
        mappaProva.put(c22, new Position(2, 2));
        mappaProva.put(c32, new Position(3, 2));


        doors.add(new Door(c00, c01));
        doors.add(new Door(c00, c10));
        doors.add(new Door(c10, c11));
        doors.add(new Door(c30, c31));
        doors.add(new Door(c21, c22));
        doors.add(new Door(c12, c22));

        return new Billboard(mappaProva,doors);
    }

}