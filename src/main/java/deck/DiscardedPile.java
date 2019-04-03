package deck;

import java.util.*;

/**
 * 
 */
public class DiscardedPile extends AbstractDeck {

    //il costruttore di default non serve perchè già presente nella sovraclasse



    /**
     * @param card  
     * @return
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

}