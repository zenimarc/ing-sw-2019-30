package board;

import board.billboard.BillboardGenerator;
import deck.AmmoCard;
import deck.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import static constants.Color.RED;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board = new Board(8, BillboardGenerator.createBillboard(2));
    Player player = new Player("Marco");

    @Test
    void cardTest(){
        Cell cell = board.getBillboard().getCellFromPosition(new Position(0,0));
        Card card = cell.getCard(0);
        board.giveCardFromCell(cell, player, 0);
        assertEquals(0, player.getPowerups().size());
        int ammo = ((AmmoCard) card).getSpecificAmmo(0);
        assertEquals(ammo+1, player.getBullets().get(RED));
        int[] array = {2, 0, 1};
        AmmoCard ammoCard = new AmmoCard(array, false);
        board.addCardInCell(ammoCard, cell);
        assertEquals(ammoCard, cell.getCard(0));

        cell = board.getBillboard().getCellFromPosition(new Position(1,0));
        card = cell.getCard(1);
        board.giveCardFromCell(cell, player, 1);
        assertEquals(1, player.getWeapons().size());
    }

}