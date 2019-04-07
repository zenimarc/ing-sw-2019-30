package deck;

import java.util.*;

/**
 * 
 */
public class Deck {

    private List<Card> cards;

    public Deck() {
        this.cards = null;
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

    public void shuffle() {
        Random number = new Random();
        Collections.shuffle(this.cards, number);
    }

    /**
     * @return
     */
    public Card draw() {
        // TODO implement here
        return null;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * @param cards 
     * @return
     */
    public void addAll(List<Card> cards) {
        for(Card card: cards){
            this.addCard(card);
        }

    }

    public void removeCard(){
        this.cards.remove(0);
    }

}