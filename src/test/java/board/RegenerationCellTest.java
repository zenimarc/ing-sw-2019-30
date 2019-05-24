package board;

import board.Cell.RegenerationCell;
import constants.Color;
import org.junit.jupiter.api.Test;
import player.Player;
import weapon.EnumWeapon;
import weapon.SimpleWeapon;
import weapon.WeaponCard;

import static org.junit.jupiter.api.Assertions.*;

class RegenerationCellTest {
    private RegenerationCell cell = new RegenerationCell(Color.RED);
    private WeaponCard weapon1 = new SimpleWeapon(EnumWeapon.ELECTROSCYTHE);
    private WeaponCard weapon2 = new SimpleWeapon(EnumWeapon.LOCK_RIFLE);
    private WeaponCard weapon3 = new SimpleWeapon(EnumWeapon.MACHINE_GUN);
    private Player player = new Player("Marco");

    @Test

    void verifyCards(){
        WeaponCard[] weapons = new WeaponCard[3];
        weapons[0] = weapon1;
        weapons[1] = weapon2;
        weapons[2] = weapon3;
        //verifies if cards are set
        cell.setCards(weapons);
        assertEquals(weapon2, weapons[1]);
        assertEquals(weapon3, weapons[2]);
        //verifies if the card is given to player and removed from cell
        cell.giveCard(player, 1);
        assertEquals(weapon2, player.getWeapons().get(0));
        cell.removeCard(cell.getCard(1));
        cell.setCard(weapon3);
        assertEquals(weapon3, cell.getCard(1));
    }


}