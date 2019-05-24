package controller;

import board.*;
import board.Cell.Cell;
import board.Cell.NormalCell;
import board.Cell.RegenerationCell;
import board.billboard.Billboard;
import constants.Color;
import org.junit.jupiter.api.BeforeEach;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO fare i test

class PlayerControllerTest {
    private static Billboard myBillboard;
    private static HashMap<Cell, Position> mappaProva = new HashMap<>();
    private static ArrayList<Door> doors = new ArrayList<>();
    private static Player player;
    private static Player p2;
    private static Player p3;
    private static Player p4;
    private static Board myBoard;
    private static PlayerController controller;
    private static PlayerController controller2;
    private static PlayerController controller3;
    private static PlayerController controller4;
    private static BoardController control;


    private static Cell c00 = new NormalCell(Color.BLUE);
    private static Cell c10 = new RegenerationCell(Color.RED);

    private static Cell c01 = new NormalCell(Color.BLUE);
    private static Cell c11 = new NormalCell(Color.RED);
    private static Cell c21 = new NormalCell(Color.WHITE);


    private static Cell c02 = new RegenerationCell(Color.BLUE);
    private static Cell c12 = new NormalCell(Color.PURPLE);
    private static Cell c22 = new NormalCell(Color.WHITE);

    private static Cell c13 = new NormalCell(Color.YELLOW);
    private static Cell c23 = new RegenerationCell(Color.YELLOW);

    @BeforeEach
    public void init(){

        //CREATE A MAP

        mappaProva.put(c00, new Position(0, 0));
        mappaProva.put(c10, new Position(1, 0));

        mappaProva.put(c01, new Position(0, 1));
        mappaProva.put(c11, new Position(1, 1));
        mappaProva.put(c21, new Position(2, 1));

        mappaProva.put(c02, new Position(0, 2));
        mappaProva.put(c12, new Position(1, 2));
        mappaProva.put(c22, new Position(2, 2));

        mappaProva.put(c13, new Position(1, 3));
        mappaProva.put(c23, new Position(2, 3));

        doors.add(new Door(c00, c10));
        doors.add(new Door(c02, c12));
        doors.add(new Door(c11, c21));
        doors.add(new Door(c11, c12));
        doors.add(new Door(c12, c13));
        doors.add(new Door(c22, c23));

        myBillboard = new Billboard(mappaProva,doors);
        myBoard = new Board(1, myBillboard);

        player = new Player("Marco", myBoard);
        controller = new PlayerController(player);
        controller.setBillboard(myBillboard);

        p2 = new Player("Christian", myBoard);
        controller2 = new PlayerController(p2);
        controller2.setBillboard(myBillboard);

        p3 = new Player("Giovanni", myBoard);
        controller3 = new PlayerController(p3);
        controller3.setBillboard(myBillboard);

        p4 = new Player("Paolo", myBoard);
        controller4 = new PlayerController(p4);
        controller4.setBillboard(myBillboard);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        List<PlayerController> PlayerControllers = new ArrayList<>();
        PlayerControllers.add(controller);
        PlayerControllers.add(controller2);
        PlayerControllers.add(controller3);
        PlayerControllers.add(controller4);

        control = new BoardController(players, myBoard, PlayerControllers);
        controller.setBoardController(control);
        controller3.setBoardController(control);
    }

  /*  @Test
    void move(){
        player.getPawn().setCell(c00);
        p3.getPawn().setCell(c00);

        //not final Frenzy
        assertFalse(control.isFinalFrenzy());
        assertTrue(controller3.move(c11, 10));
        assertTrue(controller3.move(c11, 10));
        assertFalse(controller3.move(c13, 11));
        assertFalse(controller3.move(c13, 12));

        //not final Frenzy, but player is damaged
        p3.addDamage(player, 7);
        assertTrue(controller3.move(c13, 11));
        assertTrue(controller3.move(c23, 12));

        //final frenzy double turn
        control.changeTurn();
        control.changeTurn();
        control.getBoard().decrementSkull();
        assertTrue(control.isFinalFrenzy());
        control.setFinalFrenzyTurns();
        assertEquals(2, control.getFrenzyturns());

        assertTrue(controller3.move(c13, 10));
        assertFalse(controller3.move(c10, 11));
        assertTrue(controller3.move(c11, 11));
        assertFalse(controller3.move(c13, 12));
        assertFalse(controller3.move(c01, 12));
        assertTrue(controller3.move(c21, 12));

        //final frenzy single turn
        control.changeTurn();
        control.changeTurn();
        assertFalse(control.verifyTwoTurnsFrenzy());
        assertFalse(controller.move(c01, 10));
        assertFalse(controller.move(c23, 11));
        assertTrue(controller.move(c12, 11));
        assertTrue(controller.move(c01, 12));
        assertFalse(controller.move(c13, 12));
    }


    @Test

    void Grab(){
        player.setPawnCell(c00);
        assertFalse(control.isFinalFrenzy());
        c01.setCard(new AmmoCard(new int[]{2, 0, 1}, false));
        assertTrue(controller.grab(c01, 0));
        int[] array = {2, 0, 0};
        assertTrue(player.useAmmo(array));
        assertFalse(player.useAmmo(array));
        WeaponCard weapon1 = new SimpleWeapon(ELECTROSCYTHE);
        c10.setCard(weapon1);
        assertFalse(controller.grab(c10, 0));
        c01.setCard(new AmmoCard(new int[]{2, 1, 1}, false));


    }

*/

}