package deck;

import constants.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import powerup.PowerCard;
import powerup.PowerUp;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void shuffle() {
    }

    @Test
    void draw() {
        Bullet bullet1 = new Bullet(Color.WHITE);
        PowerCard card1 = new PowerCard(bullet1, PowerUp.GUNSIGHT);
        deck.addCard(card1);
        assertEquals(card1, deck.draw());
    }

    @Test
    void addCardTest() {
        Card card1 = new PowerCard(new Bullet(Color.WHITE), PowerUp.GUNSIGHT);
        deck.addCard(card1);
        assertEquals(1, deck.getSize());
    }

    @Test
    void addAll() {
    }

    @Test
    void removeCard() {
    }
}