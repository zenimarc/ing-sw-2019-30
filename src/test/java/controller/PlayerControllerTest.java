package controller;

import board.*;
import deck.AmmoCard;
import deck.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

//TODO fare i test

class PlayerControllerTest {
    private static Billboard myBillboard;
    private static HashMap<Cell, Position> mappaProva = new HashMap<>();
    private static ArrayList<Door> doors = new ArrayList<>();
    private static Player player;
    private static Board myBoard;
    private static PlayerController controller;
    private static BoardController control;


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

    @BeforeEach
    public void init(){

        //CREATE A MAP

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

        myBillboard = new Billboard(mappaProva,doors);

        myBoard = new Board(1, myBillboard);
        player = new Player("Marco", myBoard);
        controller = new PlayerController(player);
        Player p2 = new Player("Christian", myBoard);
        Player p3 = new Player("Giovanni", myBoard);
        Player p4 = new Player("Paolo", myBoard);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        control = new BoardController();
        control.setBoard(myBoard);
        controller.setBoardController(control);

    }

    /* TODO: fix tests
    @Test
    void move(){
        player.getPawn().setCell(c00);
        assertTrue(control.isFinalFrenzy());
        assertFalse(controller.move(c11, 0));
    }


    @Test

    void Grab(){
        player.setCell(c00);
        assertFalse(control.isFinalFrenzy());
        c11.setCard(new AmmoCard(new int[]{2, 0, 1}, true));
        control.setFinalFrenzyTurns();
        assertTrue(controller.grab(c11, 11));
        int[] array = {2, 0, 0};
        assertTrue(player.useAmmo(array));
    }

     */

}