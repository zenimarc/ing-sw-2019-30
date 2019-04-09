package deck;

import java.util.*;

/**
 * Deck manages the cards yet to be used or discarded
 */
public class Deck {

    private List<Card> cards;

    /**
     * Constructors
     */

    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * End constructors
     */

    /**
     * This function shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * This function picks the first card from the deck
     * @return the drawn card
     */
    public Card draw() {
        return cards.get(0);
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
        for(Card card: cards){
            this.addCard(card);
        }

    }

    /**
     * this function removes the first card of the deck
     */
    public void removeCard(){
        this.cards.remove(0);
    }

}