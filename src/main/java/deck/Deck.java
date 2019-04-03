package deck;

import java.util.*;

/**
 * 
 */
public class Deck extends AbstractDeck {

    //private int totCards=0; //TODO Secondo me non serve indicare la dimensione, visto che possiamo già sfruttare la .size delle arraylist -Christian

    //il costruttore non serve perchè già definito nella sovraclasse

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

    /**
     * @param cards 
     * @return
     */
    public void addAll(List<Card> cards) {
        for(Card card: cards){
            this.cards.add(card);
        }

    }

    public void removeCard(DiscardedPile discard){
        discard.addCard(this.cards.get(0));
        this.cards.remove(0);
    }

}