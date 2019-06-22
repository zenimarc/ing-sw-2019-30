package deck;

import java.io.Serializable;
import java.util.*;

/**
 * Deck manages the cards yet to be used or discarded
 */
public class Deck implements Serializable {

    private List<Card> cards;
    private Random r = new Random();

    /**
     * Constructors
     */

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * This function returns the cards of the deck
     * @return a list of cards
     */
    public List<Card> getCards(){return this.cards;}

    /**
     * This function picks a random card from the deck
     * @return the drawn card
     */
    public Card draw() {
        int index = r.nextInt(cards.size());
        Card card = cards.get(index);
        removeCard(index);
        return card;
    }


    /**
     * this function adds a card at the end of the card list.
     * @param card to add in the deck.
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * this function calculates the number of cards in the deck
     * @return the number of cards in the deck
     */
    public int getSize(){
        return this.cards.size();
    }

    /**
     * this function adds a list of cards to the deck
     * @param cards to be added in the deck
     */

    public void addAll(List<Card> cards) {
        this.cards = cards;
        }

    /**
     * this function removes card in position "index"
     * @param index index of card to remove
     */
    private void removeCard(int index){
        this.cards.remove(index);
    }

}