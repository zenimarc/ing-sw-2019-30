package controller;

import board.*;
import board.Cell;
import board.NormalCell;
import board.RegenerationCell;
import board.billboard.Billboard;
import constants.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.*;
import java.util.stream.Collectors;

import static constants.Color.*;
import static controller.EnumTargetSet.*;
import static org.junit.jupiter.api.Assertions.*;


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
    private static Cell c10 = new RegenerationCell(BLUE);
    private static Cell c20 = new NormalCell(BLUE);
    private static Cell c30 = new NormalCell(BLUE);

    private static Cell c01 = new NormalCell(YELLOW);
    private static Cell c11 = new NormalCell(YELLOW);
    private static Cell c21 = new NormalCell(RED);
    private static Cell c31 = new RegenerationCell(RED);

    private static Cell c02 = new RegenerationCell(YELLOW);
    private static Cell c12 = new NormalCell(YELLOW);
    private static Cell c22 = new NormalCell(Color.WHITE);
    private static Cell c32 = new NormalCell();

    @BeforeAll
    public static void setup(){
        billboard = genBillboard();
    }

    @BeforeEach
    public void init(){
        p1 = new Player("Marco");
        p2 = new Player("Christian");
        p3 = new Player("Giovanni");
        p4 = new Player("Paolo");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        board = new Board(0, billboard);
        controller = new BoardController(players, board);
    }
    @Test
    void getPlayerController(){
        assertEquals(controller.getPlayerController(p1), controller.getPlayerController("Marco"));
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
        p1.setPawnCell(c01);
        p2.setPawnCell(c12);
        p3.setPawnCell(c22);
        p4.setPawnCell(c22);
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
        p3.setPawnCell(c21);
        p4.setPawnCell(c10);
        potentialTargets.add(p1);
        potentialTargets.add(p2);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c11, CARDINAL_WALL_BYPASS).containsAll(potentialTargets));
        //Testing Cardinal normal
        potentialTargets.clear();
        p1.setPawnCell(c00);
        p2.setPawnCell(c10);
        p3.setPawnCell(c02);
        p4.setPawnCell(c01); //same cell of the shooter case
        potentialTargets.add(p1);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c01, CARDINAL).containsAll(potentialTargets));
        p2.setPawnCell(c22);
        assertTrue(controller.getPotentialTargets(c01, CARDINAL).containsAll(potentialTargets));
        //test2 cardinal with different positions
        potentialTargets.clear();
        p1.setPawnCell(c20);
        p2.setPawnCell(c11);
        p3.setPawnCell(c22);
        p4.setPawnCell(c31);
        potentialTargets.add(p3);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c21, CARDINAL).containsAll(potentialTargets));
        //test not_visible
        potentialTargets.clear();
        p1.setPawnCell(c22);
        p2.setPawnCell(c11);
        p3.setPawnCell(c01);
        p4.setPawnCell(c21);
        potentialTargets.add(p1);
        potentialTargets.add(p4);
        assertTrue(controller.getPotentialTargets(c00, NOT_VISIBLE).containsAll(potentialTargets));
        //test visible_room (player position not changed)
        potentialTargets.clear();
        potentialTargets.add(p2);
        potentialTargets.add(p3);
        assertTrue(controller.getPotentialTargets(c00, VISIBLE_ROOM).containsAll(potentialTargets));
        potentialTargets.clear();
        assertTrue(controller.getPotentialTargets(c02, VISIBLE_ROOM).containsAll(potentialTargets));
    }

    @Test
    void getPotentialDestinationCellsTest(){
        p1.setPawnCell(c00);
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

    }

    @Test
    void getKineticCells(){
        p1.setPawnCell(c11);
        ArrayList<Position> potentialCells = new ArrayList<>();
        potentialCells.add(board.getBillboard().getCellPosition(c10));
        potentialCells.add(board.getBillboard().getCellPosition(c31));
        potentialCells.add(board.getBillboard().getCellPosition(c12));
        potentialCells.add(board.getBillboard().getCellPosition(c11));
        potentialCells.add(board.getBillboard().getCellPosition(c01));
        potentialCells.add(board.getBillboard().getCellPosition(c21));
        assertTrue(potentialCells.containsAll(controller.getCellsKineticRay(p1.getCell())));
    }

    @Test
    void setRegenerationCell(){
        controller.setRegenerationCell(p1, RED);
        assertEquals(p1.getCell(), c31);
        assertEquals(p1.getCell().getColor(), RED);
        controller.setRegenerationCell(p1, YELLOW);
        assertEquals(p1.getCell(), c02);
        assertEquals(p1.getCell().getColor(), YELLOW);
        controller.setRegenerationCell(p1, BLUE);
        assertEquals(p1.getCell(), c10);
        assertEquals(p1.getCell().getColor(), BLUE);
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
    void finalFrenzyFirstPlayer(){
        assertTrue(controller.isFinalFrenzy());
        controller.setFinalFrenzyTurns();
        assertFalse(controller.isFrenzyTurnBeforeFirst());
        controller.changeTurn();
        assertFalse(controller.isFrenzyTurnBeforeFirst());
        controller.changeTurn();
        assertFalse(controller.isFrenzyTurnBeforeFirst());
    }

    @Test
    void getTargets(){
        p1.setPawnCell(c00);
        p2.setPawnCell(c10);
        p3.setPawnCell(c20);
        p4.setPawnCell(c30);

        List<Player> opponents = controller.getPotentialTargets(p1.getCell(), VISIBLE, -1, 2);
        assertTrue(opponents.contains(p2));
        assertTrue(opponents.contains(p3));
        assertFalse(opponents.contains(p4));

        opponents = controller.getPotentialTargets(p1.getCell(), VISIBLE, 2, -1);
        assertFalse(opponents.contains(p2));
        assertTrue(opponents.contains(p3));
        assertTrue(opponents.contains(p4));

        opponents = controller.getPotentialTargets(p1.getCell(), VISIBLE, 2, 3);
        assertFalse(opponents.contains(p2));
        assertTrue(opponents.contains(p3));
        assertTrue(opponents.contains(p4));

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