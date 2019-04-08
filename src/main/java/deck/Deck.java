package deck;

import java.util.*;

/**
 * 
 */
public class Deck {

    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**public static void main(String [] args){
        Deck test = new Deck();
        Bullet bullet1 = new Bullet(Color.WHITE);
        PowerCard Card1 = new PowerCard(bullet1, PowerUp.GUNSIGHT);
        test.cards.add(Card1);
        Bullet bullet2 = new Bullet(Color.YELLOW);
        PowerCard Card2 = new PowerCard(bullet2, PowerUp.GUNSIGHT);
        test.cards.add(Card1);
        Bullet bullet3 = new Bullet(Color.RED);
        PowerCard Card3 = new PowerCard(bullet3, PowerUp.GUNSIGHT);
        test.cards.add(Card1);
        for(Card card: test.cards){
            System.out.print("Color 1:"+ test.cards.Bullet);
        }
    }**/

    /**
     * this function shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
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