/*package view;

import attack.SimpleAttack;
import board.*;
import constants.EnumString;
import controller.EnumTargetSet;
import deck.AmmoCard;
import deck.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import weapon.SimpleWeapon;
import weapon.WeaponCard;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static weapon.EnumWeapon.LOCK_RIFLE;
import static weapon.EnumWeapon.MACHINE_GUN;

class BoardViewTest {
    private static Billboard myBillboard;
    private static HashMap<Cell, Position> mappaProva = new HashMap<>();
    private static ArrayList<Door> doors = new ArrayList<>();

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
    BoardView boardView = new BoardView();



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


        doors.add(new Door(c00, c01));
        doors.add(new Door(c00, c10));
        doors.add(new Door(c10, c11));
        doors.add(new Door(c30, c31));
        doors.add(new Door(c21, c22));
        doors.add(new Door(c12, c22));

        myBillboard = new Billboard(mappaProva,doors);



    }

    @Test

    public void drawCLI() {
        Player p = new Player("Marco");
        p.setCell(c00);
        c00.addPawn(p.getPawn());
        Player p2 = new Player("Paolo");
        p2.setCell(c10);
        c10.addPawn(p2.getPawn());
        int[] array = {0, 1, 2};
        AmmoCard ammo = new AmmoCard(array, true);
        c00.setCard(ammo);
        Board board = new Board(8, myBillboard);
        boardView.setBoard(board);
        boardView.drawCLI();
    }

    @Test

    public void testName() {
        AmmoCard ammo = new AmmoCard();
        c00.setCard(ammo);
        Board board = new Board(8, myBillboard);
        boardView.setBoard(board);
        boardView.drawCLI();
    }

    //WeaponCard weapon = new SimpleWeapon(LOCK_RIFLE);

}*/


/*
    ┌──────────────────────────────────────────────────────────────────────────┐
    │ Table Heading                                                            │
    ├──────────────────┬──────────────────┬──────────────────┬─────────────────┤
    │ first row (col1) │ with some        │ and more         │ even more       │
    │                  │ information      │ information      │                 │
    ├──────────────────┼──────────────────┼──────────────────┼─────────────────┤
    │ second row       │ with some        │ and more         │ even more       │
    │ (col1)           │ information      │ information      │                 │
    │                  │ (col2)           │ (col3)           │                 │
    └──────────────────┴──────────────────┴──────────────────┴─────────────────┘
 */
