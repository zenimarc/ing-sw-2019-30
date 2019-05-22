package deck;

import board.Billboard;
import board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardFactoryTest {

    @Test

    void AmmoCardFactory(){
        Board board= new Board(8, new Billboard());
        assertEquals(36, board.getAmmoCardDeck().getSize());
        assertEquals(board.getAmmoCardDeck().getCards().get(0),board.getAmmoCardDeck().getCards().get(12));
        assertEquals(board.getAmmoCardDeck().getCards().get(8),board.getAmmoCardDeck().getCards().get(35));
    }

    @Test

    void PowerUpFactory(){
        Board board= new Board(8, new Billboard());
        assertEquals(24, board.getPowerUpDeck().getSize());
        assertEquals(board.getAmmoCardDeck().getCards().get(0),board.getAmmoCardDeck().getCards().get(12));
        assertEquals(board.getAmmoCardDeck().getCards().get(3),board.getAmmoCardDeck().getCards().get(15));
    }

}