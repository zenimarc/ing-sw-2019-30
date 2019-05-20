package view;

import board.*;
import deck.AmmoCard;
import deck.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static weapon.EnumWeapon.*;

import player.Player;
import weapon.SimpleWeapon;
import weapon.WeaponCard;


import java.util.ArrayList;
import java.util.HashMap;

class BoardViewTest {
    private static Billboard myBillboard;
    private static HashMap<Cell, Position> mappaProva = new HashMap<>();
    private static ArrayList<Door> doors = new ArrayList<>();

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
    public void init() {

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

        myBillboard = new Billboard(mappaProva, doors);
    }

    @Test

    public void drawCLI() {

        Player p = new Player("Marco");
        p.setCell(c00);
        c00.addPawn(p.getPawn());
        Player p2 = new Player("Paolofdvsgfdrgrehgerh");
        p2.setCell(c10);
        c10.addPawn(p2.getPawn());
        int[] array = {0, 1, 2};
        AmmoCard ammo = new AmmoCard(array, true);
        c00.setCard(ammo);
        WeaponCard weapon1 = new SimpleWeapon(ELECTROSCYTHE);
        WeaponCard weapon2 = new SimpleWeapon(ZX_2);
        WeaponCard weapon3 = new SimpleWeapon(LOCK_RIFLE);
        c23.setCard(weapon1);
        c23.setCard(weapon2);
        c23.setCard(weapon3);
        assertNotNull(c23.getCard(0));
        Board board = new Board(8, myBillboard);
        BoardView boardView = new BoardView(board);
        boardView.drawCLI();
    }
}

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

