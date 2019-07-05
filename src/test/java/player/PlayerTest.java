package player;

import board.Board;
import board.Cell;
import board.NormalCell;
import constants.Color;
import deck.Bullet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import powerup.PowerCard;
import powerup.PowerUp;
import weapon.EnumWeapon;
import weapon.SimpleWeapon;
import weapon.WeaponCard;

import static constants.Color.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Board board;
    private Player p1;

    @BeforeEach
    void setUp(){
        board = new Board();
        p1 = new Player("marco");
    }

    @Test
    void getPawn() {
        Pawn pawn = p1.getPawn();
        assertNotEquals(pawn, null);
    }

    @Test
    void getName() {
        assertEquals("marco", p1.getName());
    }

    @Test
    void getPlayerBoard() {
        PlayerBoard playerBoard = p1.getPlayerBoard();
        assertNotEquals(playerBoard, null);
    }

    @Test
    void addPointsTest() {
        p1.addPoints(3);
        assertEquals(3, p1.getPoints());
        p1.addPoints(1);
        assertEquals(4, p1.getPoints());
        p1.addPoints(0);
        assertEquals(4, p1.getPoints());
    }

    @Test
    void getWeapons() {
    }

    @Test
    void getPowerups() {
    }

    @Test
    void useAmmo() {
        int[] ammo = {4, 1, 3};
        int[] cost = {2, 0, 1};
        p1.addAmmo(ammo);
        assertEquals(3, p1.getBullets().get(RED));
        assertEquals(2, p1.getBullets().get(YELLOW));
        assertEquals(3, p1.getBullets().get(BLUE));
        p1.addAmmo(ammo);
        assertEquals(3, p1.getBullets().get(RED));
        assertEquals(3, p1.getBullets().get(YELLOW));
        assertEquals(3, p1.getBullets().get(BLUE));
        assertTrue(p1.useAmmo(cost));
        assertEquals(1, p1.getBullets().get(RED));
        assertEquals(3, p1.getBullets().get(YELLOW));
        assertEquals(2, p1.getBullets().get(BLUE));
    }

    @Test
    void addAmmo() {
        int[] ammo = {1, 1, 2};
        p1.addAmmo(ammo);
        assertEquals(2, p1.getBullets().get(RED));
        assertEquals(2, p1.getBullets().get(YELLOW));
        assertEquals(3, p1.getBullets().get(BLUE));
        int[] cost = {-1, -1, 0};
        p1.addAmmo(cost);
        assertEquals(1, p1.getBullets().get(RED));
        assertEquals(1, p1.getBullets().get(YELLOW));
        assertEquals(3, p1.getBullets().get(BLUE));
        p1.addAmmo(new int[]{4, 4, 4});
        assertEquals(3, p1.getBullets().get(RED));
        assertEquals(3, p1.getBullets().get(YELLOW));
        assertEquals(3, p1.getBullets().get(BLUE));
    }

    @Test

    void changePosition(){
        Cell cell = new NormalCell(RED);
        Cell cell2 = new NormalCell(RED);
        p1.setPawnCell(cell);
        assertEquals(cell.getPawns().get(0), p1.getPawn());
        p1.setPawnCell(cell2);
        assertEquals(cell2.getPawns().get(0), p1.getPawn());
        assertEquals(0, cell.getPawns().size());
    }

    @Test
    void toString1() {
    }

    @Test
    void rmCardTest(){
        assertTrue(p1.getWeapons().isEmpty());
        WeaponCard weapon = new SimpleWeapon(EnumWeapon.ELECTROSCYTHE);
        p1.getWeapons().add(weapon);
        assertEquals(1, p1.getWeapons().size());
        assertEquals(weapon, p1.getWeapons().get(0));
        p1.rmWeapon(0);
        assertTrue(p1.getWeapons().isEmpty());
    }
    @Test
    void setNotLoadWeapon(){
        WeaponCard weapon = new SimpleWeapon(EnumWeapon.ELECTROSCYTHE);
        p1.getWeapons().add(weapon);
        p1.getWeapons().get(0).setNotLoaded();
        assertFalse(p1.getWeapons().get(0).isReady());
        p1.getWeapons().get(0).setLoaded();
        assertTrue(p1.getWeapons().get(0).isReady());
        p1.setNotLoadWeapon(weapon);
        assertFalse(p1.getWeapons().get(0).isReady());
    }
    @Test
    void resetDamages(){
        Player opponent = new Player("pippo");
        p1.addDamage(opponent);
        p1.resetDamage();
        assertEquals(0, p1.getNumDamages());
    }
    @Test
    void usePowerUp(){
        PowerCard pup = new PowerCard(new Bullet(Color.RED), PowerUp.TARGETING_SCOPE);
        assertTrue(p1.getPowerups().isEmpty());
        p1.addPowerCard(pup);
        assertFalse(p1.getPowerups().isEmpty());
        p1.usePowerUp(pup, true);
        assertTrue(p1.getPowerups().isEmpty());
    }
    @Test
    void canPayPowerUp(){
        PowerCard pup = new PowerCard(new Bullet(Color.RED), PowerUp.TAGBACK_GRENADE);
        p1.useAmmo(new int[]{1,1,1});
        assertFalse(p1.canPayPowerUp(pup));
        p1.addAmmo(new int[]{1,1,1});
        assertTrue(p1.canPayPowerUp(pup));
        p1.addAmmo(new int[]{1,1,1});
        assertTrue(p1.canPayGunsight(RED));
    }
}