package view;
/*
import board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import weapon.EnumWeapon;
import weapon.SimpleWeapon;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardViewTest {
    Board gameBoard;
    Player p1, p2, p3, p4, p5;
    PlayerBoardView p1view;

    @BeforeEach
    void setUp() {
        gameBoard = new Board(1);
        p1 = new Player("Marco");
        p2 = new Player("Giorgio389729878831718332oijjoi");
        p3 = new Player("Chiara");
        p4 = new Player("Paolo");
        p5 = new Player("Massimo");
        p1.addDamage(new Player("Alberto"), 8);
        p1.addDamage(new Player("L"), 6);
        p1.addMark(p4, 2);
        p1.addMark(p2, 4);
        p1.addMark(p3, 6);
        p1.addMark(p5, 3);
        p1view = new PlayerBoardView(p1);
    }

    @Test
    void drawDamageTrack() {
        p1.addWeapon(new SimpleWeapon(EnumWeapon.LOCK_RIFLE));
        p1.addWeapon(new SimpleWeapon(EnumWeapon.LOCK_RIFLE));
        p1view.drawPlayerboard();
    }


    @Test
    void stringTrunkerTest(){
        assertEquals("ciao", p1view.stringTrunker("ciaone", 4));
        assertEquals("ci  ", p1view.stringTrunker("ci", 4));
        assertEquals("          ", p1view.stringTrunker(" ", 10));
    }
}*/