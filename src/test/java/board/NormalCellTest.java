package board;

import deck.AmmoCard;
import deck.Color;
import deck.PowerCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalCellTest {

    private AmmoCard ammoCard;
    private NormalCell normalCell1, normalCell2;

    @BeforeEach
    public void init(){
        ammoCard = new AmmoCard();
        normalCell1 = new NormalCell(Color.BLUE,ammoCard);
        normalCell2 = new NormalCell();
    }

    @Test
    void getCard() {
        assertEquals(normalCell1.getCard(0), ammoCard);
        assertNotEquals(normalCell2.getCard(0), ammoCard);
    }

    @Test
    void removeCard() {
        //ammocard == cell.ammocard
        assertEquals(normalCell1.removeCard(), ammoCard);
        assertEquals(normalCell1.getCard(0), null);
        //ammocard != cell.ammocard
        normalCell1.setCard(ammoCard);
        assertEquals(normalCell1.removeCard(new AmmoCard()), null);
        assertEquals(normalCell1.getCard(0), ammoCard);
    }

    @Test
    void removeCard1() {
        assertEquals(normalCell1.removeCard(), ammoCard);
        assertEquals(normalCell1.getCard(0), null);
    }

    @Test
    void setCard() {
        //card is not Ammocard && cellAmmocard is null
        assertFalse(normalCell2.setCard(new PowerCard()));
        //card is Ammocard && cellAmmocard is null
        assertTrue(normalCell2.setCard(ammoCard));
        normalCell2.removeCard();
        //card is not Ammocard && cellAmmocard is full
        assertFalse(normalCell2.setCard(new PowerCard()));
    }

}